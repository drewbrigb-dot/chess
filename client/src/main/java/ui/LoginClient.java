package ui;

import Server.ServerFacade;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.AuthData;
import model.GameInfo;
import model.UserData;
import ui.EscapeSequences.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class LoginClient {
    /*register() {

        //write code that calls register function
        try {
            //call serverFacade.registerUser -> username and authToken
            //print joined successfully as username
        } catch (Exception e) {
            //401 in terms of register means username already taken
            //so if e.status is 400
                //print error, username already taken
        }
    }*/

        private final ServerFacade server;
        private State state = State.SIGNEDOUT;
        private AuthData authData;
        private ArrayList<GameInfo> listOfGame;

        public LoginClient(ServerFacade server, AuthData auth) {
            this.server = server;
            this.authData = auth;
        }

    public boolean run() {
        System.out.println(" Welcome to Chess homie/homegirl.");
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
            if(state== State.SIGNEDIN) {
                return true;
            }
        }
        System.out.println();
        return false;

    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "create game" -> createGame(params);
                case "list games" -> listGames();
                case "play game" -> playGame(params);
                case "observe game" -> observeGame(params);
                case "help" -> help();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private void printPrompt() {
        System.out.print( RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String help() {
        return SET_TEXT_COLOR_YELLOW + """
                - logout - to log your butt out of this game
                - create game <GAMENAME>  - well you gotta make a game dummy before you can play
                - list games - what? you don't remember what games you made? it's just you on here
                - play game <GAMEID> <COLOR> - yeah go ahead play your stupid chess
                - observe game <GAMEID> - are you that bored?
                - help - bc ur still a lil baby
                """;
    }

    public String logout () throws Exception {
        server.logout(authData.authToken());
        state = State.SIGNEDOUT;
        return "Logout successful!";
        //throw new Exception("Expected: <yourname> <password>");
    }

    public String createGame (String ... params) throws Exception {
        if (params.length >= 1) {
            String gameName = params[0];
            Integer gameID;
            gameID = server.createGame(authData.authToken(),gameName);
            return "Game created successfully!";
        }
        throw new Exception("Expected: <yourname> <password>");
    }

    public String listGames () throws Exception {
        ArrayList<GameInfo> listGame = server.listGames(authData.authToken());
        listOfGame = listGame;
        String returnString = "";
        Integer gameNum = 1;
        for (GameInfo game : listGame) {
            returnString += gameNum + " " + game.gameName() + " " + "White player: " + game.whiteUsername();
            returnString += " " + "Black player: " + game.blackUsername() + "\n";
            gameNum++;
        }
        return "There's your list of games. You're welcome.";
    }

    public String playGame (String ... params) throws Exception {
        if (params.length >= 1) {
            String game = params[0];
            String color = params[1];
            ChessGame.TeamColor teamColor = null;

            if (color.equalsIgnoreCase("white") ) {
                teamColor = ChessGame.TeamColor.WHITE;
            }else if (color.equalsIgnoreCase("black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            }

            if (listOfGame.isEmpty()) {
                listOfGame = server.listGames(authData.authToken());
            }


            int arrayListID = Integer.parseInt(game) - 1;

            Integer gameID = listOfGame.get(arrayListID).gameID();


            server.joinGame(teamColor, gameID,authData.authToken());
            return "Join game successful! Joined game number " + gameID.toString() + "as color " + teamColor.toString();
        }
        throw new Exception("Expected: <yourname> <password>");
    }

    public String observeGame (String ... params) throws Exception {
        if (params.length >= 1) {
            String game = params[0];
            String color = params[1];
            ChessGame.TeamColor teamColor = null;

            if (color.equalsIgnoreCase("white") ) {
                teamColor = ChessGame.TeamColor.WHITE;
            }else if (color.equalsIgnoreCase("black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            }

            if (listOfGame.isEmpty()) {
                listOfGame = server.listGames(authData.authToken());
            }


            int arrayListID = Integer.parseInt(game) - 1;

            Integer gameID = listOfGame.get(arrayListID).gameID();


            server.joinGame(teamColor, gameID,authData.authToken());
            return "Join game successful! Joined game number " + gameID.toString() + "as color " + teamColor.toString();
        }
        throw new Exception("Expected: <yourname> <password>");
    }





}