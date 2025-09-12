package chess;

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
        // pawn
        ChessPosition pos  = new ChessPosition(0,0);
            //ROW 1
        ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        int i = 2;
        for (int j =1; j < 9; j++) {
            pos = new ChessPosition(i,j);
            addPiece(pos,piece);
        }
            //ROW 7
        i = 7;
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for (int j =1; j < 9; j++) {
            pos = new ChessPosition(i,j);
            addPiece(pos,piece);
        }

        //Rook
            //White
        i = 1;
        pos = new ChessPosition( i , 1);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(pos,piece);
        pos = new ChessPosition( i , 8);
        addPiece(pos,piece);
            //Black
        i = 8;
        pos = new ChessPosition( i , 1);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(pos,piece);
        pos = new ChessPosition( i , 8);
        addPiece(pos,piece);

        //Knight
            //WHITE
        i=1;
        pos = new ChessPosition( i , 2);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(pos,piece);
        pos = new ChessPosition( i , 7);
        addPiece(pos,piece);

            //BLACK
        i=8;
        pos = new ChessPosition( i , 2);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(pos,piece);
        pos = new ChessPosition( i , 7);
        addPiece(pos,piece);

        //Bishop
            //WHITE
        i=1;
        pos = new ChessPosition( i , 3);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(pos,piece);
        pos = new ChessPosition( i , 6);
        addPiece(pos,piece);

            //BLACK
        i=8;
        pos = new ChessPosition( i , 3);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(pos,piece);
        pos = new ChessPosition( i , 6);
        addPiece(pos,piece);

        //QUEEN
            //WHITE
        i=1;
        pos = new ChessPosition( i , 4);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPiece(pos,piece);
            //BLACK
        i=8;
        pos = new ChessPosition( i , 4);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPiece(pos,piece);

        //KING
            //WHITE
        i=1;
        pos = new ChessPosition( i , 5);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPiece(pos,piece);

            //BLACK
        i=8;
        pos = new ChessPosition( i , 5);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPiece(pos,piece);






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
