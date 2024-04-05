package server;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

@WebSocket
public class WSHandler {
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        session.getRemote().sendString("\nWebSocket response: " + message + " - Test successful");
    }

}
