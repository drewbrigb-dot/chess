package websocket.messages;

import chess.ChessGame;

public class NotificationMessage extends ServerMessage{
    private static final ServerMessageType type = ServerMessageType.NOTIFICATION;
    private String message;
    public NotificationMessage(String message) {
        super(type);
        this.message = message;

    }
}
