package chess;

import chess.ChessPiece.PieceType;

import java.util.ArrayList;
import java.util.HashSet;

public class PieceMovesCalculator {
    HashSet<ChessMove> moves = new HashSet<>();

    public HashSet kingMovesCalculator (ChessPosition position, ChessBoard board) {
        int directions[][] = {{1,1},{-1,1},{-1,-1},{1,-1},{1,0},{0,1},{-1,0},{0,-1}};
        for (int i =0; i <directions.length; i++) {
            int row = position.getRow() + directions[i][0];
            int col = position.getColumn() + directions[i][1];

            if (row < 9 && row > 0 && col < 9 && col > 0) {
                ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
                ChessPosition positionNew = new ChessPosition(row, col);


                if (board.getPiece(positionNew) == null) {
                    ChessMove move = new ChessMove(position, positionNew, null);
                    moves.add(move);
                } else if (board.getPiece(positionNew).getTeamColor() != color) {
                    ChessMove move = new ChessMove(position, positionNew, null);
                    moves.add(move);
                }

            }
        }
        return moves;
    }


    public HashSet queenMovesCalculator (ChessPosition position, ChessBoard board){
        int directions[][] = {{1,1},{-1,1},{-1,-1},{1,-1},{1,0},{0,1},{-1,0},{0,-1}};

        return bishopAndQueenHelper(board,position,directions);
    }

    public HashSet knightMovesCalculator(ChessPosition position, ChessBoard board) {
        int directions[][] = {{2,1},{1,2}, {-1,2},{-2,1},{-1,-2},{-2,-1}, {1,-2},{2,-1}};
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        for (int i = 0; i < directions.length; i++) {
            int row = position.getRow() + directions[i][0];
            int col = position.getColumn() + directions[i][1];

            if (row < 9 && row > 0 && col < 9 && col >0) {
                ChessPosition positionNew = new ChessPosition(row,col);

                if (board.getPiece(positionNew)== null) {
                    ChessMove move = new ChessMove(position, positionNew, null);
                    moves.add(move);
                } else if (board.getPiece(positionNew).getTeamColor() != color) {
                    ChessMove move = new ChessMove(position, positionNew, null);
                    moves.add(move);
                }
            }
        }

        return moves;
    }

    public HashSet pawnMovesCalculator (ChessPosition position, ChessBoard board) {
        int directions[][] = {{1, 0}, {1, 1}, {1, -1}};
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<PieceType> promoType = new ArrayList<>();
        promoType.add(PieceType.QUEEN);
        promoType.add(PieceType.ROOK);
        promoType.add(PieceType.BISHOP);
        promoType.add(PieceType.KNIGHT);

        if (color == ChessGame.TeamColor.WHITE) {
            for (int i = 0; i < directions.length; i++) {
                int row = position.getRow() + directions[i][0];
                int col = position.getColumn() + directions[i][1];

                if (row < 9 && row > 0 && col < 9 && col > 0) {
                    if (directions[i][1] == 0) {
                        ChessPosition positionNew = new ChessPosition(row, col);
                        if (row == 3) {
                            int rowSpecial = row + 1;
                            pawnHelper2(row, rowSpecial, col, board, positionNew, position);
                        }
                        int rowEqual = 8;
                        pawnHelper(row, rowEqual, board, positionNew, position, promoType);
                    } else {
                        ChessPosition positionNew = new ChessPosition(row, col);

                        if (board.getPiece(positionNew) != null && board.getPiece(positionNew).getTeamColor() != color) {
                            int rowEqual = 8;
                            pawnHelper3(row, rowEqual, position, positionNew, promoType);
                        }
                    }
                }

            }
        } else {
            for (int i = 0; i < directions.length; i++) {
                int row = position.getRow() - directions[i][0];
                int col = position.getColumn() - directions[i][1];

                if (row < 9 && row > 0 && col < 9 && col > 0) {
                    if (directions[i][1] == 0) {
                        ChessPosition positionNew = new ChessPosition(row, col);
                        if (row == 6) {
                            int rowSpecial = row - 1;
                            pawnHelper2(row, rowSpecial, col, board, positionNew, position);
                        }
                        int rowEqual = 1;
                        pawnHelper(row, rowEqual, board, positionNew, position, promoType);
                    } else {
                        ChessPosition positionNew = new ChessPosition(row, col);

                        if (board.getPiece(positionNew) != null && board.getPiece(positionNew).getTeamColor() != color) {
                            int rowEqual = 1;
                            pawnHelper3(row, rowEqual, position, positionNew, promoType);
                        }

                    }


                }


            }
        }
    return moves;
}

    public HashSet bishopMovesCalculator (ChessPosition position, ChessBoard board){
        int directions[][] = {{1,1},{-1,1},{-1,-1},{1,-1}};
        return bishopAndQueenHelper(board,position,directions);
    }

    public HashSet rookMovesCalculator (ChessPosition position, ChessBoard board){
        int directions[][] = {{1,0},{0,1},{-1,0},{0,-1}};
        return bishopAndQueenHelper(board,position,directions);


    }
    private Boolean addMoves (ChessBoard board, ChessPosition positionNew, ChessPosition position, ChessGame.TeamColor color) {
        if (board.getPiece(positionNew) == null) {
            ChessMove move = new ChessMove(position, positionNew, null);
            moves.add(move);
        }else if (board.getPiece(positionNew).getTeamColor() != color){
            ChessMove move = new ChessMove(position, positionNew, null);
            moves.add(move);
            return Boolean.TRUE;
        }else {
            return Boolean.TRUE;
        }
        return false;


    }

    private HashSet bishopAndQueenHelper (ChessBoard board, ChessPosition position, int directions[][]) {
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        for (int i = 0; i < directions.length; i++) {
            int row = position.getRow() + directions[i][0];
            int col = position.getColumn() + directions[i][1];

            while (row < 9 && row > 0 && col < 9 && col > 0) {
                ChessPosition positionNew = new ChessPosition(row, col);

                if (addMoves(board, positionNew, position, color)) {
                    break;
                }

                if (directions[i][0] > 0) {
                    row++;
                } else if (directions[i][0] < 0) {
                    row--;
                }
                if (directions[i][1] > 0) {
                    col++;
                } else if (directions[i][1] < 0) {
                    col--;
                }
            }
        }
        return moves;
    }

    void pawnHelper(int row, int rowEqual, ChessBoard board, ChessPosition positionNew, ChessPosition position, ArrayList<PieceType> promoType) {
        if (row == rowEqual ) {
            if (board.getPiece(positionNew) == null) {
                for (PieceType type : promoType) {
                    ChessMove move = new ChessMove(position, positionNew, type);
                    moves.add(move);
                }
            }
        } else if (board.getPiece(positionNew) == null) {
            ChessMove move = new ChessMove(position, positionNew, null);
            moves.add(move);
        }
    }

    void pawnHelper2 (int row, int rowSpecial, int col, ChessBoard board,ChessPosition positionNew, ChessPosition position) {
        if (board.getPiece(positionNew) == null) {
            ChessMove move = new ChessMove(position, positionNew, null);
            moves.add(move);
            ChessPosition positionNewSpecial = new ChessPosition(rowSpecial, col);
            if (board.getPiece(positionNewSpecial) == null) {
                move = new ChessMove(position, positionNewSpecial, null);
                moves.add(move);
            }
        }
    }

    void pawnHelper3 (int row, int rowEqual, ChessPosition position, ChessPosition positionNew, ArrayList<PieceType> promoType) {
        if (row == rowEqual) {
            for (PieceType type : promoType) {
                ChessMove move = new ChessMove(position, positionNew, type);
                moves.add(move);
            }
        } else {
            ChessMove move = new ChessMove(position, positionNew, null);
            moves.add(move);
        }
    }

}