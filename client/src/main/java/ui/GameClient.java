package ui;

import Server.ServerFacade;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import model.GameInfo;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

import java.util.ArrayList;

public class GameClient {
    //private final ServerFacade server;
    private AuthData authData;
    private ArrayList<GameInfo> listOfGame;
    ChessGame.TeamColor teamColor;
    ChessBoard board;







    public GameClient(ChessGame.TeamColor color, ChessBoard board) {
        teamColor = color;
        this.board = board;
    }

    public void run() {
        System.out.println("You made it to the Game Client. Now go back!");
        String [][] boardString = boardToString(board);

        printWhiteBoard(boardString);

        /*if (teamColor == ChessGame.TeamColor.BLACK) {
            printBlackBoard(boardString);
        }else {
            printWhiteBoard(boardString);
        }*/
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

    private void printWhiteBoard (String[][] board) {
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + "   a  b  c  d  e  f  g  h");
        //System.out.print(RESET_TEXT_COLOR +RESET_BG_COLOR);
        for (int i=0; i<8;i++) {
            int col = 8-i;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + col+ " ");
            for (int j=0;j<8;j++){
                System.out.print(SET_BG_COLOR_WHITE + SET_BG_COLOR_BLACK +" " + board[i][j].charAt(0) + " ");
            }
            System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + " " + col + " ");
        }

    }

    private void printBlackBoard(String[][]board) {

    }


}
