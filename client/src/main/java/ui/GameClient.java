package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import model.GameInfo;

import static ui.EscapeSequences.*;

import java.util.ArrayList;

public class GameClient {
    //private final ServerFacade server;
    private AuthData authData;
    private ArrayList<GameInfo> listOfGame;
    ChessGame.TeamColor teamColor;
    public ChessBoard board;
    public ChessBoard reverseBoard;







    public GameClient(ChessGame.TeamColor color, ChessBoard board) {
        teamColor = color;
        this.board = board;
    }

    public void run() {
        System.out.println("You made it to the Game Client. Now go back!");


        String [][] boardString = boardToString(board);
        reverseBoard = new ChessBoard(board.reverseBoard(board));
        String[][] boardReverseString = boardToString(reverseBoard);


        if (teamColor == ChessGame.TeamColor.BLACK) {
            printBlackBoard(boardReverseString,reverseBoard);
        }else {
            printWhiteBoard(boardString,board);
        }
    }

    private String [][] boardToString (ChessBoard board) {
        String [][] boardString = new String[8][8];

        for (int i=1; i<=8; i++) {
            for (int j=1; j<=8;j++) {
                ChessPosition position = new ChessPosition(i,j);
                if(board.getPiece(position) != null) {
                    if (board.getPiece(position).getPieceType() == (ChessPiece.PieceType.KNIGHT)) {
                        boardString[i - 1][j - 1] = "N";
                    }else {
                        boardString[i - 1][j - 1] = board.getPiece(position).toString();
                    }
                }else {
                    boardString[i - 1][j - 1] = " ";
                }
            }
        }
        return boardString;
    }

    private void printBlackBoard (String[][] board, ChessBoard reverseBoard) {
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + "   h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR +RESET_BG_COLOR);
        for (int i=7; i>=0;i--) {
            int col = 8 - i;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + col + " ");

            if (i%2 == 0) {
                printWhiteFirst(i, board,reverseBoard);
            }else {
                printBlackFirst(i, board,reverseBoard);
            }

            System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + " " + col + " " + RESET_BG_COLOR);
        }
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + "   h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
    }

    private void printWhiteBoard (String[][] board, ChessBoard regBoard) {
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + "   a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR +RESET_BG_COLOR);
        for (int i=7; i>=0;i--) {
            int col = i + 1;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + col + " ");

            if (i%2 == 0) {
                printWhiteFirst(i, board,regBoard);
            }else {
                printBlackFirst(i, board,regBoard);
            }

            System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + " " + col + " " + RESET_BG_COLOR);
        }
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + "   a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
    }


    private void printWhiteFirst (int i, String stringBoard[][],ChessBoard chessBoard) {

        for (int j=0;j<8;j++){
            printHelper(i,j,chessBoard);
            if (j%2 == 0) {
                System.out.print(SET_BG_COLOR_BLACK + " " + stringBoard[i][j].charAt(0) + " ");
            }else {
                System.out.print(SET_BG_COLOR_WHITE + " " + stringBoard[i][j].charAt(0) + " ");
            }
        }
    }


    private void printBlackFirst (int i,String stringBoard[][], ChessBoard chessBoard) {
        for (int j=0;j<8;j++){
            printHelper(i,j,chessBoard);
            if (j%2 == 0) {
                System.out.print( SET_BG_COLOR_WHITE + " " + stringBoard[i][j].charAt(0) + " ");
            }else {
                System.out.print(SET_BG_COLOR_BLACK + " " + stringBoard[i][j].charAt(0) + " ");
            }
        }
    }

    private void printHelper(int i,int j, ChessBoard chessBoard) {
        ChessPosition position = new ChessPosition(i+1,j+1);
        if (chessBoard.getPiece(position) != null) {
            ChessGame.TeamColor color = chessBoard.getPiece(position).getTeamColor();
            if (color == ChessGame.TeamColor.WHITE) {
                System.out.print(SET_TEXT_COLOR_BLUE);
            } else {
                System.out.print(SET_TEXT_COLOR_RED);
            }
        }
    }
}
