package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private static final ServerMessageType type = ServerMessageType.ERROR;
    private String message;
    public ErrorMessage(String message) {
        super(type);
        this.message = message;

    }
}
