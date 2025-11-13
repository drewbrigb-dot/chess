import Server.ServerFacade;
import chess.*;
import model.AuthData;
import ui.GameClient;
import ui.LoginClient;
import ui.LoginClientReturn;
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
            ChessBoard board = new ChessBoard();
            board.resetBoard();
            LoginClientReturn joinGameInfo = null;
            ServerFacade server = new ServerFacade(serverUrl);
            authData = new PreLoginClient(server).run();
            while (authData != null) {
                joinGameInfo = new LoginClient(server, authData).run();
                if (joinGameInfo.gameJoined()) {
                    new GameClient(joinGameInfo.color(),board).run();
                }else {
                    authData = new PreLoginClient(server).run();
                }
            }

        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}