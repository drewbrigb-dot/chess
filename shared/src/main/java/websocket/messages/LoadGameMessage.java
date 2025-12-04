package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    private static final ServerMessageType MESSAGE_TYPE = ServerMessageType.LOAD_GAME;
    private ChessGame game;
    public LoadGameMessage(ChessGame game) {
        super(MESSAGE_TYPE);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
