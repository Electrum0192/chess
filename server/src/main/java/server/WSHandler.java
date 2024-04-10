package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.util.HashSet;

@WebSocket
public class WSHandler {

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
        } else if (command.getCommandType().equals(UserGameCommand.CommandType.MAKE_MOVE)) {
            session.getRemote().sendString("TODO: MOVE");
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
            session.getRemote().sendString("TODO: RESIGN");
        }
    }

    private void messageAll(int gameID, ServerMessage serverMessage) throws IOException {
        HashSet<Session> sessions = Server.getSessions(gameID);
        String message = new Gson().toJson(serverMessage);
        for(var i : sessions){
            i.getRemote().sendString(message);
        }
    }

}
