package ui;

import Server.ServerFacade;
import model.AuthData;
import model.UserData;
import ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class PreLoginClient {
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

    public PreLoginClient(ServerFacade server) {
        this.server = server;
    }

public void run() {
    System.out.println(" Welcome to Chess homie/homegirl.");
    System.out.print(help());

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
        printPrompt();
        String line = scanner.nextLine();

        try {
            result = eval(line);
            System.out.print(BLUE + result);
        } catch (Throwable e) {
            var msg = e.toString();
            System.out.print(msg);
        }
    }
    System.out.println();

}

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <yourname>
                    - quit
                    """;
        }
        return """
                - register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                - login <USERNAME> <PASSWORD> - to play chess
                - quit - playing chess cause u suck
                - help - bc ur just a lil baby
                """;
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> rescuePet(params);
                case "quit" -> listPets();
                case "help" -> signOut();
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
            AuthData authData = server.register(userData);

            return String.format("You signed in as %s.", visitorName);
        }
        throw new Exception(Exception.Code.ClientError, "Expected: <yourname>");
    }

}
