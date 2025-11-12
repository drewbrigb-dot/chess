package ui;

import Server.ServerFacade;
import chess.ChessBoard;
import chess.ChessGame;
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

    private static final int BOARD_SIZE_IN_SQUARES = 3;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    // Padded characters.
    private static final String EMPTY = "   ";
    private static final String X = " X ";
    private static final String O = " O ";



    public GameClient(ChessGame.TeamColor color, ChessBoard board) {
        teamColor = color;
        this.board = board;
    }

    public void run() {
        System.out.println("You made it to the Game Client. Now go back!");
    }

}
