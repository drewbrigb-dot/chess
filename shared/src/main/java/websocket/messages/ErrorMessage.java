package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private static final ServerMessageType messageType = ServerMessageType.ERROR;
    private String errorMessage;
    public ErrorMessage(String message) {
        super(messageType);
        errorMessage = message;

    }
}
