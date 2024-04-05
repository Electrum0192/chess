package webSocketMessages.serverMessages;

public class Notification extends ServerMessage{
    private String message;
    public Notification(String message){
        this.message = message;
        this.serverMessageType = ServerMessageType.NOTIFICATION;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
