package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN

    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        var moves = new HashSet<ChessMove>();

        //6 sub-classes
        if (getPieceType() == type.KING) {
            //ClassName objectName = new ClassName(constructorArguments);
            PieceMovesCalculator kingMoves = new PieceMovesCalculator ();
            moves = kingMoves.KingMovesCalculator(myPosition, board);
        }
        if (getPieceType() == type.KNIGHT) {
            //ClassName objectName = new ClassName(constructorArguments);
            PieceMovesCalculator kingMoves = new PieceMovesCalculator ();
            moves = kingMoves.KnightMovesCalculator(myPosition, board);
        }
        if (getPieceType() == type.QUEEN) {
            //ClassName objectName = new ClassName(constructorArguments);
            PieceMovesCalculator queenMoves = new PieceMovesCalculator ();
            moves = queenMoves.QueenMovesCalculator(myPosition, board);
        }
        if (getPieceType() == type.ROOK) {
            //ClassName objectName = new ClassName(constructorArguments);
            PieceMovesCalculator rookMoves = new PieceMovesCalculator ();
            moves = rookMoves.RookMovesCalculator(myPosition, board);
        }
        if (getPieceType() == type.BISHOP) {
            //ClassName objectName = new ClassName(constructorArguments);
            PieceMovesCalculator bishopMoves = new PieceMovesCalculator ();
            moves = bishopMoves.BishopMovesCalculator(myPosition, board);
        }

        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessPiece piece)) {
            return false;
        }
        return pieceColor == piece.pieceColor && type == piece.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
