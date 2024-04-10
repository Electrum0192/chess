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
import webSocketMessages.userCommands.JoinPlayer;
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
            session.getRemote().sendString("TODO: OBSERVE");
        } else if (command.getCommandType().equals(UserGameCommand.CommandType.MAKE_MOVE)) {
            session.getRemote().sendString("TODO: MOVE");
        } else if (command.getCommandType().equals(UserGameCommand.CommandType.LEAVE)) {
            session.getRemote().sendString("TODO: LEAVE");
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
