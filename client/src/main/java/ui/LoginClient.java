package ui;

import server.ServerFacade;
import chess.ChessGame;
import model.AuthData;
import model.GameInfo;

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
        LoginClientReturn loginClientReturn;

        public LoginClient(ServerFacade server, AuthData auth) {
            this.server = server;
            this.authData = auth;
            loginClientReturn = new LoginClientReturn(false,null);

        }

    public LoginClientReturn run() {
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
            if(loginClientReturn.gameJoined()) {
                return loginClientReturn;
            }
        }
        System.out.println();
        return loginClientReturn;

    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout();
                case "creategame" -> createGame(params);
                case "listgames" -> listGames();
                case "playgame" -> playGame(params);
                case "observegame" -> observeGame(params);
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

    private void printPrompt() {
        System.out.print( RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String help() {
        return SET_TEXT_COLOR_YELLOW + """
                - logout - to log your butt out of this game
                - creategame <GAMENAME>  - well you gotta make a game dummy before you can play
                - listgames - what? you don't remember what games you made? it's just you on here
                - playgame <GAMEID> <COLOR> - yeah go ahead play your stupid chess
                - observegame <GAMEID> - are you that bored?
                - help - bc ur still a lil baby
                """;
    }

    public String logout () throws Exception {
        server.logout(authData.authToken());
        state = State.SIGNEDOUT;
        System.console().printf( "Logout successful!\n");
        return "quit";
        //throw new Exception("Expected: <yourname> <password>");
    }

    public String createGame (String ... params) throws Exception {
        if (params.length == 1) {
            String gameName = params[0];
            Integer gameID;
            gameID = server.createGame(authData.authToken(),gameName);
            return "Game created successfully!\n";
        }else {
            return "Please enter only one arguement: <gameID>\n";
        }
    }

    public String listGames () throws Exception {
        listOfGame = server.listGames(authData.authToken());

        if (listOfGame.isEmpty()) {
            return "There's no games right now. Go on! Make one! don't be shy.\n";
        }

        String returnString = "";
        Integer gameNum = 1;
        for (GameInfo game : listOfGame) {
            returnString += gameNum + " " + game.gameName() + " " + "White player: " + game.whiteUsername();
            returnString += " " + "Black player: " + game.blackUsername() + "\n";
            gameNum++;
        }
        return returnString + "There's your list of games. You're welcome.\n";
    }

    public String playGame (String ... params) throws Exception {
        if (params.length == 2) {
            if (listOfGame == null) {
                listOfGame = server.listGames(authData.authToken());
            }
            String game = params[0];
            if (Integer.parseInt(game) <= 0 || Integer.parseInt(game) > listOfGame.size()) {
                return "Not a valid gameID, please try again\n";
            }
            String color = params[1];
            if (!color.equalsIgnoreCase("white") && !color.equalsIgnoreCase("black")) {
                return "it's either white or black, don't think too hard about it <gameID> <color> \n";
            }
            ChessGame.TeamColor teamColor = null;

            if (color.equalsIgnoreCase("white") ) {
                teamColor = ChessGame.TeamColor.WHITE;
            }else if (color.equalsIgnoreCase("black")) {
                teamColor = ChessGame.TeamColor.BLACK;
            }

            int arrayListID = Integer.parseInt(game) - 1;

            Integer gameID = listOfGame.get(arrayListID).gameID();

            try {
                server.joinGame(teamColor, gameID, authData.authToken());
            }catch (Exception ex) {
                if (ex.getMessage().equals("Error: already taken")) {
                    return "That spot is already taken. Try the other color or a different game. Or go for a walk couch potato.\n";
                }
            }
            boolean gameJoined = true;
            loginClientReturn = new LoginClientReturn(gameJoined,teamColor);
            return "Join game successful! Joined game number " + gameID.toString() + " as color " + teamColor.toString() + "\n";
        }else {
            return "do you even want to play at this point: <gameID> <color>\n";
        }
    }

    public String observeGame (String ... params) throws Exception {
        if (params.length == 1) {

            String game = params[0];
            ChessGame.TeamColor teamColor = null;

            if (listOfGame==null) {
                listOfGame = server.listGames(authData.authToken());
                if (listOfGame.isEmpty()) {
                    return "There's no games right now. Go on! Make one! don't be shy.\n";
                }
            }

            int arrayListID = Integer.parseInt(game);

            if (arrayListID < 1 || arrayListID > listOfGame.size()) {
                return "No gameIDs of value: " + arrayListID +" Try again bro. \n";
            }
            //Integer gameID = listOfGame.get(arrayListID);


            boolean gameJoined = true;
            loginClientReturn = new LoginClientReturn(gameJoined,null);
            return "Observing game..." + arrayListID + "\n";
        }
        return "just tell me the game you want to watch, it's not twitch out here: <gameID>\n";
    }





}