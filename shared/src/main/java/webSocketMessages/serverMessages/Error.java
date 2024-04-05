package webSocketMessages.serverMessages;

public class Error extends ServerMessage{
    private String errorMessage;
    public Error(String errorMessage){
        this.errorMessage = errorMessage;
        this.serverMessageType = ServerMessageType.ERROR;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
