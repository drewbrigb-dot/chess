package ui;

import Websocket.NotificationHandler;
import Websocket.WebsocketFacade;
import chess.*;
import model.AuthData;
import model.GameInfo;
import server.ServerFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import static ui.EscapeSequences.*;


import java.util.*;

public class GameClient implements NotificationHandler {
    //private final ServerFacade server;
    private String authToken;
    private ArrayList<GameInfo> listOfGame;
    ChessGame.TeamColor teamColor;
    public ChessBoard board;
    public ChessBoard reverseBoard;
    private final ServerFacade server;
    public int gameID;
    private final WebsocketFacade ws;
    ChessGame game;

    public GameClient(ServerFacade server, ChessGame.TeamColor color, ChessBoard board, int gameID,
                      String authToken,String serverUrl) throws Exception {
        this.server = server;
        teamColor = color;
        this.gameID = gameID;
        this.authToken = authToken;
        ws = new WebsocketFacade(serverUrl, this);
    }

    public void run() {
        connectToGame();
        System.out.println("You made it to the Game Client. Let's see how long you last.\n");

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
                case "redraw" -> redraw(null);
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
    private String redraw(Collection<ChessMove> listOfMoves) {
        String[][] boardString = boardToString(board);
        reverseBoard = new ChessBoard(board.reverseBoard(board));
        String[][] boardReverseString = boardToString(reverseBoard);
        System.out.println("\n");
        if (teamColor == ChessGame.TeamColor.BLACK) {
            printBlackBoard(boardReverseString, reverseBoard,listOfMoves);
            System.out.println("Board reprint successful!\n");
            return"";

        } else {
            printWhiteBoard(boardString, board,listOfMoves);
            return "Board reprint successful!\n";
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

    private void printBlackBoard (String[][] board, ChessBoard reverseBoard, Collection<ChessMove> listOfMoves) {
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + "   h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR +RESET_BG_COLOR);
        for (int i=7; i>=0;i--) {
            int col = 8 - i;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + col + " ");

            if (i%2 == 0) {
                printWhiteFirst(i, board,reverseBoard, listOfMoves);
            }else {
                printBlackFirst(i, board,reverseBoard, listOfMoves);
            }

            System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + " " + col + " " + RESET_BG_COLOR);
        }
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + "   h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
    }

    private void printWhiteBoard (String[][] board, ChessBoard regBoard,Collection<ChessMove> listOfMoves) {
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + "   a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR +RESET_BG_COLOR);
        for (int i=7; i>=0;i--) {
            int col = i + 1;
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + col + " ");

            if (i%2 == 0) {
                printWhiteFirst(i, board,regBoard,listOfMoves);
            }else {
                printBlackFirst(i, board,regBoard,listOfMoves);
            }

            System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + " " + col + " " + RESET_BG_COLOR);
        }
        System.out.println(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_MAGENTA + "   a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
    }


    private void printWhiteFirst (int i, String stringBoard[][],ChessBoard chessBoard,Collection<ChessMove> listOfMoves) {

        for (int j=0;j<8;j++){
            boolean validMove = false;
            printHelper(i,j,chessBoard);
            ChessPosition position = new ChessPosition(i,j);
            if (listOfMoves != null) {
                for (ChessMove move : listOfMoves) {
                    ChessPosition positionCheck = move.getEndPosition();
                    int newRow =positionCheck.getRow() - 1;
                    int newCol = positionCheck.getColumn() -1;
                    ChessPosition corrPosition = new ChessPosition(newRow,newCol);
                    if (corrPosition.equals(position)) {
                        System.out.print(SET_BG_COLOR_YELLOW + " " + stringBoard[i][j].charAt(0) + " ");
                        validMove = true;
                    }
                }
            }
            if (validMove) {continue;}
            if (j%2 == 0) {
                System.out.print(SET_BG_COLOR_BLACK + " " + stringBoard[i][j].charAt(0) + " ");
            }else {
                System.out.print(SET_BG_COLOR_WHITE + " " + stringBoard[i][j].charAt(0) + " ");
            }
        }
    }


    private void printBlackFirst (int i,String stringBoard[][], ChessBoard chessBoard,Collection<ChessMove> listOfMoves) {
        for (int j=0;j<8;j++){
            boolean validMove = false;
            printHelper(i,j,chessBoard);
            ChessPosition position = new ChessPosition(i,j);
            if (listOfMoves != null) {
                for (ChessMove move : listOfMoves) {
                    ChessPosition positionCheck = move.getEndPosition();
                    int newRow =positionCheck.getRow() - 1;
                    int newCol = positionCheck.getColumn() -1;
                    ChessPosition corrPosition = new ChessPosition(newRow,newCol);
                    if (corrPosition.equals(position)) {
                        System.out.print(SET_BG_COLOR_YELLOW + " " + stringBoard[i][j].charAt(0) + " ");
                        validMove = true;
                    }
                }
            }
            if (validMove) {continue;}
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



    public String move (String ... params) throws Exception {
        if (params.length == 2) {
            String start = params[0];
            String end = params[1];

            char row = start.charAt(0);
            int rowInt = Character.toLowerCase(row) - 'a' + 1;
            char colStart = start.charAt(1);
            int colInt = Character.getNumericValue(colStart);
            ChessPosition positionStart = new ChessPosition(colInt,rowInt);

            row = end.charAt(0);
            rowInt = Character.toLowerCase(row) - 'a' + 1;
            char colEnd = end.charAt(1);
            colInt = Character.getNumericValue(colEnd);
            ChessPosition positionEnd = new ChessPosition(colInt,rowInt);

            ChessMove move = new ChessMove(positionStart,positionEnd,null);

            ws.makeMove(authToken,gameID,move);
            return "Move made successfully weirdo\n";
        }else {
            return "Please enter two chess locations like 'A2 D4': <String,String>\n";
        }
    }

    private String resign () throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print( RESET_TEXT_COLOR + "Are you sure you want to resign sonny boy? <yes or no>\n" + SET_TEXT_COLOR_GREEN);
        System.out.print( RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
        String line = scanner.nextLine();
        if (Objects.equals(line, "yes")){
            ws.resign(authToken,gameID);
            return "";
        }else if (Objects.equals(line, "no")) {
            return "make up your mind then";
        }else {
            return "Valid input required dumbo";
        }
    }

    public String highlight (String ... params) throws Exception {
        if (params.length == 1) {
            String start = params[0];

            char col = start.charAt(0);
            int colInt = Character.toLowerCase(col) - 'a' + 1;
            char row = start.charAt(1);
            int rowInt = Character.getNumericValue(row);
            ChessPosition position = new ChessPosition(rowInt, colInt);
            ChessGame gameClone = new ChessGame(game);
            Collection<ChessMove> listOfMoves;
            Collection<ChessMove> reverseList = List.of();
            listOfMoves = gameClone.validMoves(position);
            if (teamColor == ChessGame.TeamColor.BLACK) {
                for (ChessMove move : listOfMoves) {
                    ChessPosition posStart = move.getStartPosition();
                    ChessPosition endPos = move.getEndPosition();
                    ChessPiece.PieceType piece = move.getPromotionPiece();
                    int oldRow = posStart.getRow();
                    int oldCol = posStart.getColumn();
                    int newRow = 9 - oldRow;
                    int newCol = 9 - oldCol;
                    ChessPosition revStart = new ChessPosition(newRow,newCol);
                    
                    oldRow = endPos.getRow();
                    oldCol = endPos.getColumn();
                    newRow = 9- oldRow;
                    newCol = 9- oldCol;
                    ChessPosition revEnd = new ChessPosition(newRow, newCol);
                    
                    ChessMove revMove = new ChessMove(revStart,revEnd,piece);
                    reverseList.add(revMove);
                }
                listOfMoves = reverseList;
            }
            redraw(listOfMoves);


            return "highlight successful nerd\n";
        }
        return "just give me your starting position boy <Row Column>\n";
    }


    private String leave() throws Exception {
        ws.leave(authToken,gameID);
        return "quit";
    }

    private void printPrompt() {
        System.out.print( RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void notifyLoad(LoadGameMessage loadGameMessage) {
        String message = "game not loaded!!";
        if (loadGameMessage.getServerMessageType()== ServerMessage.ServerMessageType.LOAD_GAME) {
            ChessGame newGame = loadGameMessage.getGame();
            game = newGame;
            board = game.getBoard();
            message = "New game loaded.\n";
            redraw(null);
        }
        System.out.println(SET_TEXT_COLOR_RED + message);
        System.out.println(help());
        printPrompt();

    }

    public void notifyError(ErrorMessage errorMessage) {
        String message = "error not received!";
        if (errorMessage.getServerMessageType()== ServerMessage.ServerMessageType.ERROR) {
            message = errorMessage.getErrorMessage();
        }
        System.out.println(SET_TEXT_COLOR_RED + message);
        printPrompt();
    }

    public void notifyNotification(NotificationMessage notificationMessage) {
        if (notificationMessage.getServerMessageType()== ServerMessage.ServerMessageType.NOTIFICATION) {
            String message = notificationMessage.getMessage();
            System.out.println(SET_TEXT_COLOR_RED + message);
            printPrompt();
        }

    }

    private String connectToGame() {
        try {
            ws.connect(authToken,gameID);
            return "Connection to game successful nerd.";
        } catch (Exception e ) {
            return e.getMessage();
        }
    }
}
