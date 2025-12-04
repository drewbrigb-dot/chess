package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    private static final ServerMessageType messageType = ServerMessageType.LOAD_GAME;
    private ChessGame game;
    public LoadGameMessage(ChessGame game) {
        super(messageType);
        this.game = game;
    }
}
