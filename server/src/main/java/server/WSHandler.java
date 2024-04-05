package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.userCommands.UserGameCommand;

@WebSocket
public class WSHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        //Decode message to a UserGameCommand
        var command = new Gson().fromJson(message, UserGameCommand.class);
        if(command.getCommandType().equals(UserGameCommand.CommandType.JOIN_PLAYER)){
            session.getRemote().sendString("TODO: JOIN");
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

}
