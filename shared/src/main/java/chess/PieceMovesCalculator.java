package chess;

import java.util.HashSet;

public class PieceMovesCalculator {
    HashSet<ChessMove> moves = new HashSet<>();

    public HashSet KingMovesCalculator (ChessPosition position, ChessBoard board) {
       int row = position.getRow();
       int col = position.getColumn();


        //ChessPosition startPosition, ChessPosition endPosition,ChessPiece.PieceType promotionPiece
        int rowOffSet = -1;

        for (int i = 0; i < 3; i++) {
            int colOffSet = -1;

            for (int j = 0; j < 3; j++) {
                if ((colOffSet == 0 && rowOffSet == 0) || (row+rowOffSet > 8) || (row+rowOffSet < 0) || (col + colOffSet > 8) || (col + colOffSet < 0)) {
                    colOffSet++;
                    continue;
                }
                if (rowOffSet + row != 0 && colOffSet + col != 0) {
                    ChessPosition end = new ChessPosition((row + rowOffSet), (col + colOffSet));
                    if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                        colOffSet++;
                        continue;
                    }
                    ChessMove potentialMove = new ChessMove(position, end, null);
                    moves.add(potentialMove);
                }

                colOffSet++;

            }
            rowOffSet++;
        }
        return moves;
    }


    public HashSet QueenMovesCalculator (ChessPosition position, ChessBoard board){
        HashSet<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();

        //cross move
            // coluumn down
        int tempRow = row - 1;
        while (tempRow !=0) {
            ChessPosition end = new ChessPosition((tempRow), col);
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position, end, null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow--;
        }
        //up column
        tempRow = row + 1;
        while (tempRow != 9) {
            ChessPosition end = new ChessPosition((tempRow), col);
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position, end, null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow++;
        }
        //column left
        int tempCol = col - 1;
        while (tempCol != 0) {
            ChessPosition end = new ChessPosition(row, tempCol);
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position, end, null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempCol--;
        }
        //column right
        tempCol = col + 1;
        while (tempCol != 9) {
            ChessPosition end = new ChessPosition(row, tempCol);
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position, end, null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempCol++;
        }

        //diagonal move
            //down-left
        tempRow = row - 1;
        tempCol = col -1;
        while (tempRow !=0 && tempCol != 0) {
            ChessPosition end = new ChessPosition((tempRow), (tempCol));
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position, end, null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow--;
            tempCol--;
        }


        //up to the right
        tempRow = row + 1;
        tempCol = col + 1;

        while (tempRow < 9 && tempCol < 9) {
            ChessPosition end = new ChessPosition((tempRow), (tempCol));
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position, end, null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow++;
            tempCol++;
        }
        //down to the right
        tempRow = row - 1;
        tempCol = col + 1;
        while (tempRow > 0 && tempCol < 9) {
            ChessPosition end = new ChessPosition((tempRow), (tempCol));
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position, end, null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow--;
            tempCol++;
        }
        //up to the left
        tempRow = row + 1;
        tempCol = col - 1;

        while (tempRow < 9 && tempCol > 0) {
            ChessPosition end = new ChessPosition((tempRow), (tempCol));
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() != board.getPiece(position).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position, end, null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow++;
            tempCol--;
        }


        return moves;
    }

    public HashSet KnightMovesCalculator(ChessPosition position, ChessBoard board) {
        HashSet<ChessMove> moves = new HashSet<>();

        int row = position.getRow();
        int col = position.getColumn();


        //ChessPosition startPosition, ChessPosition endPosition,ChessPiece.PieceType promotionPiece
        int colOffSet = -2;
        for (int i = 0; i < 5; i++) {
                //side ways L's
            if (colOffSet == 0) {
                colOffSet++;
                continue;
            }
            if (colOffSet%2 == 0) {
                int rowOffSet = -1;
                for (int j = 0; j < 2; j++) {
                    ChessPosition end = new ChessPosition(row + rowOffSet, col + colOffSet);
                    if ((row+rowOffSet > 8) || (row+rowOffSet < 1) || (col + colOffSet > 8) || (col + colOffSet < 1)) {
                        rowOffSet = rowOffSet + 2;
                        continue;
                    }
                    if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                        rowOffSet = rowOffSet + 2;
                        continue;
                    }
                    ChessMove potentialMove = new ChessMove(position, end, null);
                    moves.add(potentialMove);
                    rowOffSet = rowOffSet + 2;
                }
            }
            if(colOffSet % 2 != 0) {
                int rowOffSet = -2;
                for (int j = 0; j < 2; j++) {
                    ChessPosition end = new ChessPosition(row + rowOffSet, col + colOffSet);
                    if ((row+rowOffSet > 8) || (row+rowOffSet < 1) || (col + colOffSet > 8) || (col + colOffSet < 1)) {
                        rowOffSet = rowOffSet + 4;
                        continue;
                    }
                    if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == board.getPiece(position).getTeamColor()) {
                        rowOffSet = rowOffSet + 4;
                        continue;
                    }
                    ChessMove potentialMove = new ChessMove(position, end, null);
                    moves.add(potentialMove);
                    rowOffSet = rowOffSet + 4;
                }
            }
                colOffSet++;
            }

        return moves;
    }

    public HashSet PawnMovesCalculator (ChessPosition position, ChessBoard board){
        HashSet<ChessMove> moves = new HashSet<>();

        int row = position.getRow();
        int col = position.getColumn();

        int tempRow = row;
        int tempCol = col;
        // white initial
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {

            ChessPosition end = new ChessPosition(tempRow + 1, col);
            if (board.getPiece(end) == null)  {
                if (tempRow + 1 == 8) {
                    ChessMove potentialMove = new ChessMove(position, end, ChessPiece.PieceType.QUEEN);
                    moves.add(potentialMove);
                    potentialMove = new ChessMove(position, end, ChessPiece.PieceType.ROOK);
                    moves.add(potentialMove);
                    potentialMove = new ChessMove(position, end, ChessPiece.PieceType.KNIGHT);
                    moves.add(potentialMove);
                    potentialMove = new ChessMove(position, end, ChessPiece.PieceType.BISHOP);
                    moves.add(potentialMove);
                } else {
                    ChessMove potentialMove = new ChessMove(position, end, null);
                    moves.add(potentialMove);
                }

                //check to see if two from start is available
                if (row == 2) {
                    end = new ChessPosition(tempRow + 2, col);
                    if(board.getPiece(end) == null) {
                        ChessMove potentialMove = new ChessMove(position, end, null);
                        moves.add(potentialMove);
                    }
                }
            }
            // check to see if pawn can take piece
            if (col + 1 < 9) {
                end = new ChessPosition(tempRow + 1, col + 1);
                if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (row + 1 == 8) {
                        ChessMove potentialMove = new ChessMove(position, end, ChessPiece.PieceType.QUEEN);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.ROOK);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.KNIGHT);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.BISHOP);
                        moves.add(potentialMove);
                    } else {
                    ChessMove potentialMove = new ChessMove(position, end, null);
                    moves.add(potentialMove);
                    }
                }
            }
            if (col - 1 > 0) {
                end = new ChessPosition(tempRow + 1, col - 1);
                if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (row + 1 == 8) {
                        ChessMove potentialMove = new ChessMove(position, end, ChessPiece.PieceType.QUEEN);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.ROOK);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.KNIGHT);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.BISHOP);
                        moves.add(potentialMove);
                    } else {
                        ChessMove potentialMove = new ChessMove(position, end, null);
                        moves.add(potentialMove);
                    }
                }
            }

        }

        //black initial
        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.BLACK) {

            ChessPosition end = new ChessPosition(tempRow - 1, col);
            if (board.getPiece(end) == null) {
                if (tempRow - 1 == 1) {
                    ChessMove potentialMove = new ChessMove(position, end, ChessPiece.PieceType.QUEEN);
                    moves.add(potentialMove);
                    potentialMove = new ChessMove(position, end, ChessPiece.PieceType.ROOK);
                    moves.add(potentialMove);
                    potentialMove = new ChessMove(position, end, ChessPiece.PieceType.KNIGHT);
                    moves.add(potentialMove);
                    potentialMove = new ChessMove(position, end, ChessPiece.PieceType.BISHOP);
                    moves.add(potentialMove);
                } else {
                    ChessMove potentialMove = new ChessMove(position, end, null);
                    moves.add(potentialMove);
                }

                //check to see if two from start is available
                if (row == 7) {
                    end = new ChessPosition(tempRow - 2, col);
                    if (board.getPiece(end) == null) {
                        ChessMove potentialMove = new ChessMove(position, end, null);
                        moves.add(potentialMove);
                    }
                }
            }
            // check to see if pawn can take piece
            if (col + 1 < 9) {
                end = new ChessPosition(tempRow - 1, col + 1);
                if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (row - 1 == 1) {
                        ChessMove potentialMove = new ChessMove(position, end, ChessPiece.PieceType.QUEEN);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.ROOK);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.KNIGHT);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.BISHOP);
                        moves.add(potentialMove);
                    } else {
                        ChessMove potentialMove = new ChessMove(position, end, null);
                        moves.add(potentialMove);
                    }
                }
            }
            if (col - 1 > 0) {
                end = new ChessPosition(tempRow - 1, col - 1);
                if (board.getPiece(end) != null && board.getPiece(end).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    if (row - 1 == 1) {
                        ChessMove potentialMove = new ChessMove(position, end, ChessPiece.PieceType.QUEEN);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.ROOK);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.KNIGHT);
                        moves.add(potentialMove);
                        potentialMove = new ChessMove(position, end, ChessPiece.PieceType.BISHOP);
                        moves.add(potentialMove);
                    } else {
                        ChessMove potentialMove = new ChessMove(position, end, null);
                        moves.add(potentialMove);
                    }
                }
            }
        }

        return moves;
    }
    public HashSet BishopMovesCalculator (ChessPosition position, ChessBoard board){
        HashSet<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();

        // up-right
        int tempRow = row + 1;
        int tempCol = col + 1;

        while (tempRow < 9 && tempCol < 9) {
            ChessPosition end = new ChessPosition(tempRow,tempCol);

            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() == board.getPiece(end).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() != board.getPiece(end).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position,end,null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow++;
            tempCol++;

        }

        //down-right
        tempRow = row - 1;
        tempCol = col + 1;

        while (tempRow > 0 && tempCol < 9) {
            ChessPosition end = new ChessPosition(tempRow,tempCol);

            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() == board.getPiece(end).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() != board.getPiece(end).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position,end,null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow--;
            tempCol++;
        }

        //down-left
        tempRow = row - 1;
        tempCol = col - 1;

        while (tempRow > 0 && tempCol > 0) {
            ChessPosition end = new ChessPosition(tempRow,tempCol);

            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() == board.getPiece(end).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() != board.getPiece(end).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position,end,null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow--;
            tempCol--;
        }

        tempRow = row + 1;
        tempCol = col - 1;

        while (tempRow < 9 && tempCol > 0) {
            ChessPosition end = new ChessPosition(tempRow,tempCol);

            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() == board.getPiece(end).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() != board.getPiece(end).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position,end,null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow++;
            tempCol--;
        }



        return moves;
    }

    public HashSet RookMovesCalculator (ChessPosition position, ChessBoard board){
        HashSet<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();

        //move up
        int tempRow = row + 1;
        while (tempRow < 9) {
            ChessPosition end = new ChessPosition(tempRow,col);

            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() == board.getPiece(end).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() != board.getPiece(end).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position,end,null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow++;

        }
        //col Down
        tempRow = row - 1;
        while (tempRow > 0) {
            ChessPosition end = new ChessPosition(tempRow,col);

            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() == board.getPiece(end).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() != board.getPiece(end).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position,end,null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempRow--;

        }
        //row right
        int tempCol = col + 1;
        while (tempCol < 9) {
            ChessPosition end = new ChessPosition(row,tempCol);

            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() == board.getPiece(end).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() != board.getPiece(end).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position,end,null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempCol++;

        }
        tempCol = col - 1;
        while (tempCol > 0) {
            ChessPosition end = new ChessPosition(row,tempCol);

            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() == board.getPiece(end).getTeamColor()) {
                break;
            }
            if (board.getPiece(end) != null && board.getPiece(position).getTeamColor() != board.getPiece(end).getTeamColor()) {
                ChessMove potentialMove = new ChessMove(position,end,null);
                moves.add(potentialMove);
                break;
            }
            ChessMove potentialMove = new ChessMove(position, end, null);
            moves.add(potentialMove);

            tempCol--;

        }
        return moves;

    }
}

