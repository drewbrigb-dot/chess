package chess;

import java.util.HashSet;

public class PieceMovesCalculator {
    HashSet<ChessMove> moves = new HashSet<>();

    public HashSet KingMovesCalculator (ChessPosition position) {
       int row = position.getRow();
       int col = position.getColumn();
       ChessPosition end = new ChessPosition(row, col);

       // top left, going clockwise
        //ChessPosition startPosition, ChessPosition endPosition,
        //                     ChessPiece.PieceType promotionPiece
        ChessMove potentialMove = new ChessMove(position, end,ChessPiece.PieceType.KING);
        moves.add(potentialMove);
        return moves;
    }


    public class QueenMovesCalculator {

    }

    public class KnightMovesCalculator {

    }

    public class PawnMovesCalculator {

    }
    public class BishopMovesCalculator {

    }

    public class RookMovesCalculator {

    }
}

