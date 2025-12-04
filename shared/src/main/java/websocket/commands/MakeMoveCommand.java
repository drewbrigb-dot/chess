package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    private static final CommandType messageType = UserGameCommand.CommandType.MAKE_MOVE;
    private ChessMove move;
    public MakeMoveCommand (String authToken,Integer gameID, ChessMove move) {
        super(messageType,authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove(){
        return move;
    }
}

