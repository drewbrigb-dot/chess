package websocket.messages;

import chess.ChessGame;

public class NotificationMessage extends ServerMessage{
    private static final ServerMessageType messageType = ServerMessageType.NOTIFICATION;
    private String message;
    public NotificationMessage(String message) {
        super(messageType);
        this.message = message;

    }
}
