package websocket.messages;

import chess.ChessGame;

public class NotificationMessage extends ServerMessage{
    private static final ServerMessageType MESSAGE_TYPE = ServerMessageType.NOTIFICATION;
    private String message;
    public NotificationMessage(String message) {
        super(MESSAGE_TYPE);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
