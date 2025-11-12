import Server.ServerFacade;
import chess.*;
import model.AuthData;
import ui.LoginClient;
import ui.PreLoginClient;

public class Main {
    public static void main(String[] args) {
        AuthData authData = null;
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        try {
            ServerFacade server = new ServerFacade(serverUrl);
            authData = new PreLoginClient(server).run();
            if (authData != null) {
                new LoginClient(server,authData).run();
            }

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}