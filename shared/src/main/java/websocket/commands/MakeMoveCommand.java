package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    private static final CommandType Type = UserGameCommand.CommandType.MAKE_MOVE;
    private ChessMove move;
    public MakeMoveCommand (String authToken,Integer gameID, ChessMove move) {
        super(Type,authToken, gameID);
        this.move = move;
    }

    public ChessMove getMove(){
        return move;
    }
}

