package chess;

import java.util.ArrayList;
import java.util.HashSet;

public class PieceMovesCalculator {
    HashSet<ChessMove> moves = new HashSet<>();

    public HashSet KingMovesCalculator (ChessPosition position, ChessBoard board) {
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


    public HashSet QueenMovesCalculator (ChessPosition position, ChessBoard board){
        int directions[][] = {{1,1},{-1,1},{-1,-1},{1,-1},{1,0},{0,1},{-1,0},{0,-1}};
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        for (int i =0; i <directions.length; i++) {
            int row = position.getRow() + directions[i][0];
            int col = position.getColumn() + directions[i][1];

            while (row < 9 && row > 0 && col < 9 && col >0) {
                ChessPosition positionNew = new ChessPosition(row,col);

                if (board.getPiece(positionNew) == null) {
                    ChessMove move = new ChessMove(position, positionNew, null);
                    moves.add(move);
                }else if (board.getPiece(positionNew).getTeamColor() != color){
                    ChessMove move = new ChessMove(position, positionNew, null);
                    moves.add(move);
                    break;
                }else {
                    break;
                }

                if (directions[i][0] > 0) {
                    row++;
                }else if (directions[i][0] < 0) {row--;}
                if (directions[i][1] > 0) {
                    col++;
                }else if (directions[i][1] < 0) {col--;}
            }

        }
        return moves;
    }

    public HashSet KnightMovesCalculator(ChessPosition position, ChessBoard board) {
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

    public HashSet PawnMovesCalculator (ChessPosition position, ChessBoard board) {
        int directions[][] = {{1, 0}, {1, 1}, {1, -1}};
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        ArrayList<ChessPiece.PieceType> promoType = new ArrayList<>();
        promoType.add(ChessPiece.PieceType.QUEEN);
        promoType.add(ChessPiece.PieceType.ROOK);
        promoType.add(ChessPiece.PieceType.BISHOP);
        promoType.add(ChessPiece.PieceType.KNIGHT);

        if (color == ChessGame.TeamColor.WHITE) {
            for (int i = 0; i < directions.length; i++) {
                int row = position.getRow() + directions[i][0];
                int col = position.getColumn() + directions[i][1];

                if (row < 9 && row > 0 && col < 9 && col > 0) {
                    if (directions[i][1] == 0) {
                        ChessPosition positionNew = new ChessPosition(row, col);
                        if (row == 3) {
                            if (board.getPiece(positionNew) == null) {
                                ChessMove move = new ChessMove(position, positionNew, null);
                                moves.add(move);
                                ChessPosition positionNewSpecial = new ChessPosition(row + 1, col);
                                if (board.getPiece(positionNewSpecial) == null) {
                                    move = new ChessMove(position, positionNewSpecial, null);
                                    moves.add(move);
                                }
                            }
                        }
                        if (row == 8) {
                            if (board.getPiece(positionNew) == null) {
                                for (ChessPiece.PieceType type : promoType) {
                                    ChessMove move = new ChessMove(position, positionNew, type);
                                    moves.add(move);
                                }
                            }
                        } else if (board.getPiece(positionNew) == null) {
                            ChessMove move = new ChessMove(position, positionNew, null);
                            moves.add(move);
                        }
                    } else {
                        ChessPosition positionNew = new ChessPosition(row, col);

                        if (board.getPiece(positionNew) != null && board.getPiece(positionNew).getTeamColor() != color) {
                            if (row == 8) {
                                for (ChessPiece.PieceType type : promoType) {
                                    ChessMove move = new ChessMove(position, positionNew, type);
                                    moves.add(move);
                                }
                            } else {
                                ChessMove move = new ChessMove(position, positionNew, null);
                                moves.add(move);
                            }
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
                            if (board.getPiece(positionNew) == null) {
                                ChessMove move = new ChessMove(position, positionNew, null);
                                moves.add(move);
                                ChessPosition positionNewSpecial = new ChessPosition(row - 1, col);
                                if (board.getPiece(positionNewSpecial) == null) {
                                    move = new ChessMove(position, positionNewSpecial, null);
                                    moves.add(move);
                                }
                            }

                        }
                        if (row == 1) {
                            if (board.getPiece(positionNew) == null) {
                                for (ChessPiece.PieceType type : promoType) {
                                    ChessMove move = new ChessMove(position, positionNew, type);
                                    moves.add(move);
                                }
                            }
                        } else if (board.getPiece(positionNew) == null) {
                            ChessMove move = new ChessMove(position, positionNew, null);
                            moves.add(move);
                        }
                    } else {
                        ChessPosition positionNew = new ChessPosition(row, col);

                        if (board.getPiece(positionNew) != null && board.getPiece(positionNew).getTeamColor() != color) {
                            if (row == 1) {
                                for (ChessPiece.PieceType type : promoType) {
                                    ChessMove move = new ChessMove(position, positionNew, type);
                                    moves.add(move);
                                }
                            } else {
                                ChessMove move = new ChessMove(position, positionNew, null);
                                moves.add(move);
                            }
                        }

                    }


                }


            }
        }

        return moves;
    }

    public HashSet BishopMovesCalculator (ChessPosition position, ChessBoard board){
        int directions[][] = {{1,1},{-1,1},{-1,-1},{1,-1}};
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        for (int i =0; i <directions.length; i++) {
            int row = position.getRow() + directions[i][0];
            int col = position.getColumn() + directions[i][1];

            while (row < 9 && row > 0 && col < 9 && col >0) {
                ChessPosition positionNew = new ChessPosition(row,col);

                if (board.getPiece(positionNew) == null) {
                    ChessMove move = new ChessMove(position, positionNew, null);
                    moves.add(move);
                }else if (board.getPiece(positionNew).getTeamColor() != color){
                    ChessMove move = new ChessMove(position, positionNew, null);
                    moves.add(move);
                    break;
                }else {
                    break;
                }

                if (directions[i][0] > 0) {
                    row++;
                }else if (directions[i][0] < 0) {row--;}
                if (directions[i][1] > 0) {
                    col++;
                }else if (directions[i][1] < 0) {col--;}
            }

        }
        return moves;
    }

    public HashSet RookMovesCalculator (ChessPosition position, ChessBoard board){
        int directions[][] = {{1,0},{0,1},{-1,0},{0,-1}};
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        for (int i =0; i <directions.length; i++) {
            int row = position.getRow() + directions[i][0];
            int col = position.getColumn() + directions[i][1];

            while (row < 9 && row > 0 && col < 9 && col >0) {
                ChessPosition positionNew = new ChessPosition(row,col);
                if (board.getPiece(positionNew) == null) {
                    ChessMove move = new ChessMove(position, positionNew, null);
                    moves.add(move);
                }else if (board.getPiece(positionNew).getTeamColor() != color){
                    ChessMove move = new ChessMove(position, positionNew, null);
                    moves.add(move);
                    break;
                }else {
                    break;
                }

                if (directions[i][0] > 0) {
                    row++;
                }else if (directions[i][0] < 0) {row--;}
                if (directions[i][1] > 0) {
                    col++;
                }else if (directions[i][1] < 0) {col--;}
            }
        }

        return moves;
    }
}

