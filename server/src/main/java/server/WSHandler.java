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
            JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
            //Check that gameID is valid
            GameData game = new SQLGameDAO().getGame(joinPlayer.getGameID());
            if(game == null){
                Error error = new Error("Requested game does not exist.");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }
            //Check that the user is authorized for this team
            AuthData auth = new SQLAuthDAO().getAuth(joinPlayer.getAuthString());
            if(auth == null){
                Error error = new Error("You are not authorized.");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }

            String username = auth.username();
            if(joinPlayer.getPlayerColor().equals(ChessGame.TeamColor.WHITE)){
                if(game.whiteUsername() == null){
                    Error error = new Error("You are not authorized for requested team.");
                    session.getRemote().sendString(new Gson().toJson(error));
                    return;
                }
                if(!game.whiteUsername().equals(username)){
                    Error error = new Error("You are not authorized for requested team.");
                    session.getRemote().sendString(new Gson().toJson(error));
                    return;
                }
            }
            if(joinPlayer.getPlayerColor().equals(ChessGame.TeamColor.BLACK)){
                if(game.blackUsername() == null){
                    Error error = new Error("You are not authorized for requested team.");
                    session.getRemote().sendString(new Gson().toJson(error));
                    return;
                }
                if(!game.blackUsername().equals(username)){
                    Error error = new Error("You are not authorized for requested team.");
                    session.getRemote().sendString(new Gson().toJson(error));
                    return;
                }
            }
            //Add session to group for that game
            Server.addPlayer(joinPlayer.getGameID(),session);
            //Send LoadGame to user
            session.getRemote().sendString(new Gson().toJson(new LoadGame(game.game())));
            //Send Message to all users that player has joined the game
            String notif = "";
            if(joinPlayer.getPlayerColor().equals(ChessGame.TeamColor.WHITE)){
                notif = username+" has joined the game as White";
            }else{
                notif = username+" has joined the game as Black";
            }
            messageOthers(joinPlayer.getGameID(), new Notification(notif), session);
            //Make backups to remove player in case of crash
            gameIDBackup = joinPlayer.getGameID();
            usernameBackup = username;
            me = session;
        } else if (command.getCommandType().equals(UserGameCommand.CommandType.JOIN_OBSERVER)) {
            JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);

            //Check that gameID is valid
            GameData game = new SQLGameDAO().getGame(joinObserver.getGameID());
            if(game == null){
                Error error = new Error("Requested game does not exist.");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }
            //Check that the user is authorized for this team
            AuthData auth = new SQLAuthDAO().getAuth(joinObserver.getAuthString());
            if(auth == null){
                Error error = new Error("You are not authorized.");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }

            //Add session to group for that game
            Server.addPlayer(joinObserver.getGameID(),session);
            //Send LoadGame to user
            session.getRemote().sendString(new Gson().toJson(new LoadGame(game.game())));
            //Send Message to all users that player has joined the game
            String username = new SQLAuthDAO().getAuth(joinObserver.getAuthString()).username();
            String notif = username+" has joined the game as an observer";
            messageOthers(joinObserver.getGameID(), new Notification(notif), session);
            //Make backups to remove player in case of crash
            gameIDBackup = joinObserver.getGameID();
            usernameBackup = username;
            me = session;
        } else if (command.getCommandType().equals(UserGameCommand.CommandType.MAKE_MOVE)) {
            //Get move from command
            MakeMove moveCommand = new Gson().fromJson(message, MakeMove.class);
            ChessMove move = moveCommand.getMove();
            int gameID = moveCommand.getGameID();
            //Check that the move is given by the correct player
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(gameID);
            String username = new SQLAuthDAO().getAuth(moveCommand.getAuthString()).username();
            if(username.equals(game.whiteUsername()) && game.game().getTeamTurn().equals(ChessGame.TeamColor.BLACK)){
                Error error = new Error("It is not your turn.");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            } else if (username.equals(game.blackUsername()) && game.game().getTeamTurn().equals(ChessGame.TeamColor.WHITE)) {
                Error error = new Error("It is not your turn.");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            } else if (!username.equals(game.whiteUsername()) && !username.equals(game.blackUsername())) {
                Error error = new Error("Observers cannot make moves.");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }
            //Check that the move is valid, make the move if it is

            String pieceType = game.game().getBoard().getPiece(move.getStartPosition()).getPieceType().toString();
            try {
                game.game().makeMove(move);
                //Update the server and the players
                gameDAO.updateGame(gameID,new Gson().toJson(game.game()));
                messageAll(gameID, new LoadGame(game.game()));
                messageOthers(gameID,new Notification(username+" moved "+pieceType+" from "+parsePos(move.getStartPosition())+" to "+parsePos(move.getEndPosition())), session);
            }catch (InvalidMoveException e){
                if(e.getMessage() != null) {
                    session.getRemote().sendString(new Gson().toJson(new Error(e.getMessage())));
                }else{
                    session.getRemote().sendString(new Gson().toJson(new Error("Move impossible, the game has ended.")));
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
            messageOthers(leave.getGameID(), new Notification(notif), session);
        } else if (command.getCommandType().equals(UserGameCommand.CommandType.RESIGN)) {
            Resign resign = new Gson().fromJson(message, Resign.class);
            //Check that user is not an observer
            SQLGameDAO gameDAO = new SQLGameDAO();
            GameData game = gameDAO.getGame(resign.getGameID());
            String username = new SQLAuthDAO().getAuth(resign.getAuthString()).username();
            if (!username.equals(game.whiteUsername()) && !username.equals(game.blackUsername())) {
                Error error = new Error("Observers cannot resign.");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }
            //Check that game has not already ended
            if(game.game().isGameOver()){
                Error error = new Error("Game is already over.");
                session.getRemote().sendString(new Gson().toJson(error));
                return;
            }
            //End the game
            game.game().setGameOver(true);
            gameDAO.updateGame(resign.getGameID(), new Gson().toJson(game.game()));
            //Notify Players
            String notif = usernameBackup+" has forfeited. Congratulations, the game is now over.";
            messageAll(resign.getGameID(), new Notification(notif));
        }
    }

    private void messageAll(int gameID, ServerMessage serverMessage) throws IOException {
        HashSet<Session> sessions = Server.getSessions(gameID);
        String message = new Gson().toJson(serverMessage);
        if(sessions != null && !sessions.isEmpty()) {
            for (var i : sessions) {
                if(i.isOpen()) {
                    i.getRemote().sendString(message);
                }else{
                    Server.removePlayer(gameID, i);
                }
            }
        }
    }

    private void messageOthers(int gameID, ServerMessage serverMessage, Session me) throws IOException{
        HashSet<Session> sessions = Server.getSessions(gameID);
        String message = new Gson().toJson(serverMessage);
        if(sessions != null && !sessions.isEmpty()) {
            for (var i : sessions) {
                if(!i.equals(me)) {
                    if(i.isOpen()) {
                        i.getRemote().sendString(message);
                    }else{
                        Server.removePlayer(gameID, i);
                    }
                }
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
