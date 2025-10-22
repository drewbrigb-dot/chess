package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        turn = TeamColor.WHITE;
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> allmoves = new HashSet<>();
        Collection<ChessMove> allowedMoves = new HashSet<>();


        ChessPiece piece = board.getPiece(startPosition);

        if (piece != null) {
            allmoves = piece.pieceMoves(board, startPosition);
            for (ChessMove move : allmoves) {
                ChessBoard boardClone = new ChessBoard(board);

                ChessPiece clonePiece = new ChessPiece(piece.getTeamColor(), piece.getPieceType());

                ChessPosition end = move.getEndPosition();

                board.addPiece(end, clonePiece);
                board.addPiece(startPosition, null);

                if (isInCheck(clonePiece.getTeamColor()) == false) {
                    allowedMoves.add(move);
                }
                board = boardClone;
            }
        }

        return allowedMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        Collection<ChessMove> moves;


        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece movingPiece = board.getPiece(startPosition);

        boolean moveSuccess = false;


        if (board.getPiece(startPosition) != null && getTeamTurn() == movingPiece.getTeamColor()) {
            moves = validMoves(startPosition);
            TeamColor oppColor = (movingPiece.getTeamColor() == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
            for (ChessMove singleMove : moves) {
                if (move.equals(singleMove)) {
                    //checkPromotePawn(movingPiece,endPosition);
                    if (move.getPromotionPiece() != null) {
                        ChessPiece promoPiece = new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece());
                        board.addPiece(endPosition, promoPiece);
                    } else {
                        board.addPiece(endPosition, movingPiece);
                    }
                    board.addPiece(startPosition, null);
                    setTeamTurn(oppColor);
                    moveSuccess = true;
                }
            }
            if (moveSuccess == false) {
                throw new InvalidMoveException("Invalid Move");
            }
        } else {
            throw new InvalidMoveException("Invalid Move");
        }
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> moves;
        TeamColor oppColor;

        if (teamColor == TeamColor.WHITE) {
            oppColor = TeamColor.BLACK;
        } else {
            oppColor = TeamColor.WHITE;
        }

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece == null || piece.getTeamColor() != oppColor) {
                    continue;
                }
                moves = piece.pieceMoves(board, position);
                for (ChessMove move : moves) {
                    ChessPiece endPiece = board.getPiece(move.getEndPosition());
                    if (endPiece == null || endPiece.getPieceType() != ChessPiece.PieceType.KING) {
                        continue;
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //ChessPosition kingPosition = null;
        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece currPiece = board.getPiece(position);
                if (currPiece == null || currPiece.getTeamColor() != teamColor) {
                    continue;
                }
                Collection<ChessMove> pieceMoves = validMoves(position);
                for (ChessMove move : pieceMoves) {
                    ChessBoard boardClone = new ChessBoard(board);
                    try {
                        makeMove(move);
                        if (!isInCheck(teamColor)) {
                            return false;
                        }
                    } catch (InvalidMoveException e) {
                    }
                    board = boardClone;
                }
            }
        }
        return true;
    }
    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        TeamColor ogColor = getTeamTurn();
        if (isInCheck(teamColor) == true) {return false;}
        if (teamColor != getTeamTurn()) {
            setTeamTurn(teamColor);
        }

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition position = new ChessPosition(row, col);
                if (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != teamColor) {continue;}
                Collection<ChessMove> pieceMoves = validMoves(position);

                for (ChessMove move : pieceMoves) {
                    ChessBoard boardClone = new ChessBoard(board);

                    if (!tryHelper(move,teamColor)){
                        return false;
                    };
                    board = boardClone;
                }
            }
        }
        setTeamTurn(ogColor);
        return true;
    }


    private boolean tryHelper (ChessMove move, TeamColor teamColor) {
        try {
            makeMove(move);
            if (!isInCheck(teamColor)) {
                return false;
            }
        } catch (InvalidMoveException e) {
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board= board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return turn == chessGame.turn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }
}

