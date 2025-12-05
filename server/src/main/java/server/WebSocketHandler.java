package server;


import chess.*;
import com.google.gson.Gson;
//import exception.ResponseException;
import dataaccess.AuthDataAccess;
import dataaccess.DataAccessException;
import dataaccess.GameDataAccess;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.GameData;
import model.GameInfo;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;
    public WebSocketHandler (AuthDataAccess authDataAccess, GameDataAccess gameDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            MakeMoveCommand makeMoveCommand = null;
            if (command.getCommandType()== UserGameCommand.CommandType.MAKE_MOVE) {
                makeMoveCommand =  new Gson().fromJson(ctx.message(), MakeMoveCommand.class);;
            }
            Integer gameID = command.getGameID();
            switch (command.getCommandType()) {
                case CONNECT -> connect(command.getGameID(), command.getAuthToken(),ctx.session);
                case MAKE_MOVE -> makeMove(makeMoveCommand, ctx.session);
                case LEAVE -> leave(command.getGameID(), command.getAuthToken(), ctx.session);
                case RESIGN -> resign(command.getGameID(), command.getAuthToken(),ctx.session);
            }
        } catch (IOException | DataAccessException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(Integer gameID, String authToken, Session session) throws IOException, DataAccessException {
        var serializer = new Gson();
        connections.add(gameID,session);
        if (gameDataAccess.getGame(gameID) == null || authDataAccess.getAuth(authToken) == null)  {
            var message = String.format("Error! you gave me a bad ID %s, what in the world are you doing bro", gameID);
            ServerMessage badGameID = new ErrorMessage(message);
            connections.broadcastErrorToRoot(session,badGameID);
        }else {
            String username = authDataAccess.getAuth(authToken).username();
            GameData gameData = gameDataAccess.getGame(gameID);
            String teamColor="observer";
            if (Objects.equals(username, gameData.blackUsername())) {
                teamColor = "black";
            }else if (Objects.equals(username, gameData.whiteUsername())) {
                teamColor = "white";
            }
            var message = String.format("yo fool this guy wants to fight you: %s, he's playing for this squad: " +
                    "%s", authDataAccess.getAuth(authToken).username(), teamColor);
            ServerMessage loadGameToRoot = new LoadGameMessage(gameDataAccess.getGame(gameID).game());
            ServerMessage loadGameToAll = new NotificationMessage(message);
            connections.broadcastRootClient(session, loadGameToRoot, gameID);
            connections.broadcastExceptRoot(session, loadGameToAll, gameID);
        }

    }

    private void makeMove(MakeMoveCommand moveCommand, Session session) throws IOException, DataAccessException {
        if (moveCommand == null) {
            var message = String.format("Give the right command fool!");
            ServerMessage badGameID = new ErrorMessage(message);
            connections.broadcastErrorToRoot(session, badGameID);
            return;
        }
        Integer gameID = moveCommand.getGameID();
        String authToken = moveCommand.getAuthToken();
        ChessMove move = moveCommand.getMove();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        if (gameDataAccess.getGame(gameID) == null || authDataAccess.getAuth(authToken) == null) {
            String message;
            if (gameDataAccess.getGame(gameID) == null) {
                message = String.format("Error! you gave me a bad ID %s, what in the world are you doing bro", gameID);
            } else if (authDataAccess.getAuth(authToken) == null) {
                message = String.format("Error! You can't do that! what in the world are you doing bro", gameID);
            } else {
                message = String.format("Error!", gameID);
            }
            ServerMessage badGameID = new ErrorMessage(message);
            connections.broadcastErrorToRoot(session, badGameID);
        } else {
            ChessGame chessGame = gameDataAccess.getGame(gameID).game();

            if(chessGame.getGameOver()) {
                var message = String.format("Error! Game is over jitt, why you tryna change the past?");
                ServerMessage badGameID = new ErrorMessage(message);
                connections.broadcastErrorToRoot(session, badGameID);
                return;
            }

            boolean validMove = true;
            try {
                chessGame.makeMove(moveCommand.getMove());
            } catch (InvalidMoveException e) {

                validMove = false;
            }
            if (!validMove) {
                String message = String.format("Error! Bad move. Don't be a cheater");
                ServerMessage badGameID = new ErrorMessage(message);
                connections.broadcastErrorToRoot(session, badGameID);
                return;
            }

            //check to see if move is with correct team's piece
            String username = authDataAccess.getAuth(authToken).username();
            ChessGame.TeamColor userColor;
            ChessGame.TeamColor oppColor;
            ChessGame game = gameDataAccess.getGame(gameID).game();
            String blackUsername = gameDataAccess.getGame(gameID).blackUsername();
            String whiteUsername = gameDataAccess.getGame(gameID).whiteUsername();
            if (Objects.equals(blackUsername, username)){
                userColor = ChessGame.TeamColor.BLACK;
                oppColor = ChessGame.TeamColor.WHITE;
            }else if (Objects.equals(whiteUsername, username)){
                userColor = ChessGame.TeamColor.WHITE;
                oppColor = ChessGame.TeamColor.BLACK;
            }else {
                String message = String.format("Error! Hands off fella, you just said you wanted to watch buddy.");
                ServerMessage badGameID = new ErrorMessage(message);
                connections.broadcastErrorToRoot(session, badGameID);
                return;
            }
            ChessGame.TeamColor teamColor = game.getTeamTurn();
            if (teamColor != userColor) {
                String message = String.format("Error! You dirty scumbag. It's not your turn!");
                ServerMessage badGameID = new ErrorMessage(message);
                connections.broadcastErrorToRoot(session, badGameID);
                return;
            }
            //end check
            var message = String.format("%s made a move, yeah he made a moooove (Steve Lacy) \n", username);
            ChessPiece piece = game.getBoard().getPiece(start);
            message += username + " moved " + piece.getPieceType().toString() + " from " + start.toClientString() +
                    " to " + end.toClientString();
            gameDataAccess.updateGame(chessGame,gameID);
            LoadGameMessage loadGameToAll = new LoadGameMessage(gameDataAccess.getGame(gameID).game());
            NotificationMessage loadGameExceptRoot = new NotificationMessage(message);
            connections.broadcastToAll(loadGameToAll, gameID);
            connections.broadcastExceptRoot(session, loadGameExceptRoot, gameID);

            ChessGame newGame = gameDataAccess.getGame(gameID).game();

            //check for other color

            if (newGame.isInCheckmate(oppColor)) {
                String checkMessage;
                checkMessage = String.format("%s dude you're cooked. Checkmate baby.", username);
                ServerMessage notificationMessage = new NotificationMessage(checkMessage);
                connections.broadcastToAll(notificationMessage, gameID);

            }else if (newGame.isInStalemate(oppColor)) {
                String checkMessage;
                checkMessage = String.format("%s you spent all this time and looked where it got you." +
                        "Absolutely nowhere.'Twas a stalemate.", username);
                ServerMessage notificationMessage = new NotificationMessage(checkMessage);
                connections.broadcastToAll(notificationMessage, gameID);
            }else if (newGame.isInCheck(oppColor)){
                String checkMessage;
                checkMessage = String.format("%s you're almost done! Someone's in check!", username);
                ServerMessage notificationMessage = new NotificationMessage(checkMessage);
                connections.broadcastToAll(notificationMessage, gameID);
            }
        }
    }

   public void leave(Integer gameID, String authToken, Session session) throws DataAccessException, IOException {
        String username = authDataAccess.getAuth(authToken).username();
       ChessGame.TeamColor userColor = null;
       String blackUsername = gameDataAccess.getGame(gameID).blackUsername();
       String whiteUsername = gameDataAccess.getGame(gameID).whiteUsername();
       if (Objects.equals(blackUsername, username)){
           userColor = ChessGame.TeamColor.BLACK;
       }else if (Objects.equals(whiteUsername, username)){
           userColor = ChessGame.TeamColor.WHITE;
       }
       
       if (userColor == ChessGame.TeamColor.WHITE) {
           gameDataAccess.updateUsernames(null,blackUsername,gameID);
       } else if (userColor == ChessGame.TeamColor.BLACK) {
           gameDataAccess.updateUsernames(whiteUsername,null,gameID);
       }

       var message = String.format("%s left the game because he's scared",username);
        ServerMessage loadGameBroadcast = new NotificationMessage(message);
        connections.broadcastExceptRoot(session,loadGameBroadcast,gameID);
        connections.remove(gameID,session);


    }
    private void resign(Integer gameID, String authToken, Session session) throws IOException, DataAccessException {
        String username = authDataAccess.getAuth(authToken).username();
        String blackUsername = gameDataAccess.getGame(gameID).blackUsername();
        String whiteUsername = gameDataAccess.getGame(gameID).whiteUsername();
        if (!Objects.equals(blackUsername, username) && !Objects.equals(whiteUsername, username)){
            String message = String.format("Error! You probably gambled on this game didn't ya. You're pushing it buddy." +
                    "You tried to resign as an observer.");
            ServerMessage badGameID = new ErrorMessage(message);
            connections.broadcastErrorToRoot(session, badGameID);
            return;
        }

        ChessGame chessGame = gameDataAccess.getGame(gameID).game();
        if(chessGame.getGameOver()) {
            String message = String.format("Error! You're late to the game bud. It's over. It's been over bro. You " +
                    "tried to join a game that has ended.");
            ServerMessage badGameID = new ErrorMessage(message);
            connections.broadcastErrorToRoot(session, badGameID);
            return;
        }

        chessGame.setGameOver(true);
        gameDataAccess.updateGame(chessGame,gameID);
        var message = String.format("%s left the game because they know they bouta lose.",username);
        ServerMessage loadGameBroadcast = new NotificationMessage(message);
        connections.broadcastToAll(loadGameBroadcast,gameID);
    }
}
