package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.HashSet;

@WebSocket
public class WSHandler {

    int gameIDBackup = 0;
    String usernameBackup = "";
    Session me = null;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        //Decode message to a UserGameCommand
        var command = new Gson().fromJson(message, UserGameCommand.class);
        if(command.getCommandType().equals(UserGameCommand.CommandType.JOIN_PLAYER)){
            //Add session to group for that game
            JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
            Server.addPlayer(joinPlayer.getGameID(),session);
            //Send LoadGame to user
            ChessGame game = new SQLGameDAO().getGame(joinPlayer.getGameID()).game();
            session.getRemote().sendString(new Gson().toJson(new LoadGame(game)));
            //Send Message to all users that player has joined the game
            String username = new SQLAuthDAO().getAuth(joinPlayer.getAuthString()).username();
            String notif = "";
            if(joinPlayer.getPlayerColor().equals(ChessGame.TeamColor.WHITE)){
                notif = username+" has joined the game as White";
            }else{
                notif = username+" has joined the game as Black";
            }
            messageAll(joinPlayer.getGameID(), new Notification(notif));
            //Make backups to remove player in case of crash
            gameIDBackup = joinPlayer.getGameID();
            usernameBackup = username;
            me = session;
        } else if (command.getCommandType().equals(UserGameCommand.CommandType.JOIN_OBSERVER)) {
            //Add session to group for that game
            JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
            Server.addPlayer(joinObserver.getGameID(),session);
            //Send LoadGame to user
            ChessGame game = new SQLGameDAO().getGame(joinObserver.getGameID()).game();
            session.getRemote().sendString(new Gson().toJson(new LoadGame(game)));
            //Send Message to all users that player has joined the game
            String username = new SQLAuthDAO().getAuth(joinObserver.getAuthString()).username();
            String notif = username+" has joined the game as an observer";
            messageAll(joinObserver.getGameID(), new Notification(notif));
            //Make backups to remove player in case of crash
            gameIDBackup = joinObserver.getGameID();
            usernameBackup = username;
            me = session;
        } else if (command.getCommandType().equals(UserGameCommand.CommandType.MAKE_MOVE)) {
            //Get move from command
            MakeMove moveCommand = new Gson().fromJson(message, MakeMove.class);
            ChessMove move = moveCommand.getMove();
            int gameID = moveCommand.getGameID();
            //Check that the move is valid, make the move if it is
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(gameID);
            String username = null;
            if(game.game().getTeamTurn().equals(ChessGame.TeamColor.WHITE)){
                username = game.whiteUsername();
            }else if(game.game().getTeamTurn().equals(ChessGame.TeamColor.BLACK)){
                username = game.blackUsername();
            }else{username = "Server";}
            String pieceType = game.game().getBoard().getPiece(move.getStartPosition()).getPieceType().toString();
            try {
                game.game().makeMove(move);
                //Update the server and the players
                gameDAO.updateGame(gameID,new Gson().toJson(game.game()));
                messageAll(gameID, new LoadGame(game.game()));
                messageAll(gameID,new Notification(username+" moved "+pieceType+" from "+parsePos(move.getStartPosition())+" to "+parsePos(move.getEndPosition())));
            }catch (InvalidMoveException e){
                if(e.getMessage().equals("Move impossible, the game has ended.")) {
                    session.getRemote().sendString(new Gson().toJson(new Error(e.getMessage())));
                }else{
                    session.getRemote().sendString(new Gson().toJson(new Error("Move is invalid")));
                }
            }
        } else if (command.getCommandType().equals(UserGameCommand.CommandType.LEAVE)) {
            //Remove player from group for that game
            Leave leave = new Gson().fromJson(message, Leave.class);
            Server.removePlayer(leave.getGameID(),session);
            //Remove player from game list for that game, if applicable
            String username = new SQLAuthDAO().getAuth(leave.getAuthString()).username();
            ChessGame.TeamColor team = null;
            SQLGameDAO gameDAO = new SQLGameDAO();
            if(gameDAO.getGame(leave.getGameID()).whiteUsername() != null){
                if(gameDAO.getGame(leave.getGameID()).whiteUsername().equals(username)){
                    team = ChessGame.TeamColor.WHITE;
                }
            }else if(gameDAO.getGame(leave.getGameID()).blackUsername().equals(username)) {
                if(gameDAO.getGame(leave.getGameID()).blackUsername().equals(username)) {
                    team = ChessGame.TeamColor.BLACK;
                }
            }
            if(team != null) {
                gameDAO.updatePlayers(leave.getGameID(),team,null);
            }
            //Notify the other players
            String notif = username+" has left the game";
            messageAll(leave.getGameID(), new Notification(notif));
        } else if (command.getCommandType().equals(UserGameCommand.CommandType.RESIGN)) {
            Resign resign = new Gson().fromJson(message, Resign.class);
            //End the game
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(resign.getGameID());
            game.game().setGameOver(true);
            gameDAO.updateGame(resign.getGameID(), new Gson().toJson(game.game()));
            //Notify Players
            messageAll(resign.getGameID(), new LoadGame(game.game()));
            String notif = usernameBackup+" has forfeited. Congratulations, the game is now over.";
            messageAll(resign.getGameID(), new Notification(notif));
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) throws Exception{
        if(session.equals(me)) {
            Server.removePlayer(gameIDBackup, session);
            //Remove player from game list for that game, if applicable
            ChessGame.TeamColor team = null;
            SQLGameDAO gameDAO = new SQLGameDAO();
            if (gameDAO.getGame(gameIDBackup).whiteUsername() != null) {
                if (gameDAO.getGame(gameIDBackup).whiteUsername().equals(usernameBackup)) {
                    team = ChessGame.TeamColor.WHITE;
                }
            } else if (gameDAO.getGame(gameIDBackup).blackUsername().equals(usernameBackup)) {
                if (gameDAO.getGame(gameIDBackup).blackUsername().equals(usernameBackup)) {
                    team = ChessGame.TeamColor.BLACK;
                }
            }
            if (team != null) {
                gameDAO.updatePlayers(gameIDBackup, team, null);
            }
            //Notify the other players
            String notif = usernameBackup + " has timed out or crashed, and been removed from the game\nreason: " + statusCode + ": " + reason;
            messageAll(gameIDBackup, new Notification(notif));
        }
    }

    private void messageAll(int gameID, ServerMessage serverMessage) throws IOException {
        HashSet<Session> sessions = Server.getSessions(gameID);
        String message = new Gson().toJson(serverMessage);
        if(sessions != null) {
            for (var i : sessions) {
                i.getRemote().sendString(message);
            }
        }
    }

    private static String parsePos(ChessPosition in){

        int row = in.getRow();
        int column = in.getColumn();
        String out = "";
        switch (column) {
            case 8 -> out = "A";
            case 7 -> out = "B";
            case 6 -> out = "C";
            case 5 -> out = "D";
            case 4 -> out = "E";
            case 3 -> out = "F";
            case 2 -> out = "G";
            case 1 -> out = "H";
        }
        out = out+row;
        return out;
    }

}
