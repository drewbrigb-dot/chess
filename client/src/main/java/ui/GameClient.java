package ui;

import chess.*;
import model.AuthData;
import model.GameInfo;
import server.ServerFacade;

import static ui.EscapeSequences.*;

import java.util.*;

public class GameClient {
    //private final ServerFacade server;
    private String authToken;
    private ArrayList<GameInfo> listOfGame;
    ChessGame.TeamColor teamColor;
    public ChessBoard board;
    public ChessBoard reverseBoard;
    public ChessGame chessGame;
    private final ServerFacade server;
    public int gameID;

    public GameClient(ServerFacade server, ChessGame.TeamColor color, ChessBoard board, int gameID, String authToken) {
        this.server = server;
        teamColor = color;
        this.board = board;
        this.gameID = gameID;
        chessGame = new ChessGame();
        this.authToken = authToken;


    }

    public void run() {
        chessGame.setBoard(board);
        redraw();
        System.out.println("You made it to the Game Client. Now go back!");

        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }



    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                case "help" -> help();
                default -> help();
            };
        } catch (Exception ex) {
            if (ex.getMessage().startsWith("For input string:")) {
                return "Please enter a digit. Don't spell it. Don't be trying to be all smart on me now.\n";
            }
            return ex.getMessage();
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

    public String help() {
        return SET_TEXT_COLOR_RED + """
                - redraw - what? you can't see your silly game? wah wah wah
                - leave - yeah i would leave too if i was playing chess on my free time
                - move<Int,Int,Int,Int> - make a chess move, but moving from you chair from time to time probably wouldn't hurt ya
                - resign - give up chess, go touch grass 
                - highlight - highlights what moves you can make, it's not an excuse to start sniffing markers okay?
                - help - bc you've always been just a stupid baby
                """;
    }

    private String redraw() {
        String[][] boardString = boardToString(board);
        reverseBoard = new ChessBoard(board.reverseBoard(board));
        String[][] boardReverseString = boardToString(reverseBoard);

        if (teamColor == ChessGame.TeamColor.BLACK) {
            printBlackBoard(boardReverseString, reverseBoard);
            return "Board reprint successful!";

        } else {
            printWhiteBoard(boardString, board);
            return "Board reprint successful!\n";
        }
    }

    public String move (String ... params) throws Exception {
        if (params.length == 2) {
            String start = params[0];
            String end = params[1];

            char row = start.charAt(0);
            int rowInt = Character.toLowerCase(row) - 'a' + 1;
            char col = start.charAt(1);
            int colInt = Character.getNumericValue(col);
            ChessPosition positionStart = new ChessPosition(rowInt,colInt);

            row = end.charAt(0);
            rowInt = Character.toLowerCase(row) - 'a' + 1;
            colInt = Character.getNumericValue(col);
            ChessPosition positionEnd = new ChessPosition(rowInt,colInt);

            ChessMove move = new ChessMove(positionStart,positionEnd,null);
            chessGame.makeMove(move);
            return "Move made successfully weirdo";
        }else {
            return "Please enter two chess locations like 'A2 D4': <String,String>\n";
        }
    }

    private String resign () {
        Scanner scanner = new Scanner(System.in);
        System.out.print( RESET_TEXT_COLOR + "Are you sure you want to resign sonny boy? <yes or no>\n" + SET_TEXT_COLOR_GREEN);
        System.out.print( RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
        String line = scanner.nextLine();
        if (Objects.equals(line, "yes")){
            chessGame = new ChessGame();
            return "quit";
        }else if (Objects.equals(line, "no")) {
            return "make up your mind then";
        }else {
            return "Valid input required dumbo";
        }
    }

    public String highlight (String ... params) throws Exception {
        if (params.length == 1) {
            String start = params[0];

            char row = start.charAt(0);
            int rowInt = Character.toLowerCase(row) - 'a' + 1;
            char col = start.charAt(1);
            int colInt = Character.getNumericValue(col);
            ChessPosition position = new ChessPosition(rowInt, colInt);

            Collection<ChessMove> listOfMoves = chessGame.validMoves(position);

            return "highlight successful nerd";
        }
        return "just give me your starting position boy <String>";
    }

    private String leave() throws Exception {
        server.joinGame(teamColor,gameID, authToken);
        return "quit";
    }

    private void printPrompt() {
        System.out.print( RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
