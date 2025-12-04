package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private static final ServerMessageType MESSAGE_TYPE = ServerMessageType.ERROR;
    private String errorMessage;
    public ErrorMessage(String message) {
        super(MESSAGE_TYPE);
        errorMessage = message;

    }
}
