package ui;

import Server.ServerFacade;
import model.AuthData;
import model.UserData;
import ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PreLoginClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private AuthData authData;

    public PreLoginClient(ServerFacade server) {
        this.server = server;
    }

public AuthData run() {
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
            return authData;
        }
    }
    System.out.println();
    return null;

}


    private void printPrompt() {
        System.out.print( RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                case "help" -> help();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws Exception {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            String username = params[0];
            String password = params[1];
            String email = params[2];
            UserData userData = new UserData(username,password,email);
            authData = server.register(userData);
            state=State.SIGNEDIN;
            return "Register and sign-in successful! as " + authData.username();

        }
        throw new Exception("Expected: <yourname> <password> <email>");
    }

    public String login (String ... params) throws Exception {
        if (params.length >= 1) {
            state = State.SIGNEDIN;
            String username = params[0];
            String password = params[1];
            UserData userData = new UserData(username, password, null);
            authData = server.login(userData);
            state = State.SIGNEDIN;
            return "Sign-in successful!";
        }
        throw new Exception("Expected: <yourname> <password>");
    }


    public String help() {
        return SET_TEXT_COLOR_BLUE + """
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account you stinky baby
                - login <USERNAME> <PASSWORD> - to play chess cause you have nothing else better to do
                - quit - playing chess cause u suck
                - help - bc ur just a lil baby
                """;
    }

}
