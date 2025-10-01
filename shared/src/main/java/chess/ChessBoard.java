package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board ;
    
    public ChessBoard() {
        board = new ChessPiece[8][8];
    }
    // two dimensional array
    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board [position.getRow()-1][position.getColumn()-1] = piece;


    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board [position.getRow()-1][position.getColumn()-1];

    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        ArrayList<ChessPiece.PieceType> types = new ArrayList<>();
        types.add(ChessPiece.PieceType.ROOK);
        types.add(ChessPiece.PieceType.KNIGHT);
        types.add(ChessPiece.PieceType.BISHOP);
        types.add(ChessPiece.PieceType.QUEEN);
        types.add(ChessPiece.PieceType.KING);
        types.add(ChessPiece.PieceType.BISHOP);
        types.add(ChessPiece.PieceType.KNIGHT);
        types.add(ChessPiece.PieceType.ROOK);

        //pawn
        int row = 2;
        int col = 1;

        while (col < 9) {
            ChessPosition position = new ChessPosition(row,col);
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(position,pawn);
            col++;
        }
        row = 7;
        col =1;
        while (col < 9) {
            ChessPosition position = new ChessPosition(row, col);
            ChessPiece pawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(position,pawn);
            col++;
        }
        ChessGame.TeamColor color = ChessGame.TeamColor.WHITE;
        row = 1;

        for (int j=0; j < 2; j++) {
            col = 1;
            for(ChessPiece.PieceType type: types) {
                ChessPosition position = new ChessPosition(row,col);
                ChessPiece piece = new ChessPiece(color,type);
                addPiece(position,piece);
                col++;
            }
            color = ChessGame.TeamColor.BLACK;
            row = 8;


        }


    }








    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessBoard that)) {
            return false;
        }
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
