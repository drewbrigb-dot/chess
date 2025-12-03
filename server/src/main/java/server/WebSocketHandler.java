package server;


import chess.ChessMove;
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
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;

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
            switch (command.getCommandType()) {
                case CONNECT -> connect(command.getGameID(), command.getAuthToken(),ctx.session);
               /* case MAKE_MOVE -> makeMove(command.getGameID(),command.getAuthToken(), ctx.session);
                case LEAVE -> leave(command.getGameID(), command.getAuthToken(), ctx.session);
                case RESIGN -> resign(., ctx.session);*/
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
        var message = String.format("yo %s", authDataAccess.getAuth(authToken).username());
        ServerMessage loadGameToRoot = new LoadGameMessage(gameDataAccess.getGame(gameID).game());
        ServerMessage loadGameToAll = new NotificationMessage(message);
        connections.broadcastRootClient(session, loadGameToRoot, gameID);
        connections.broadcastExceptRoot(session,loadGameToAll,gameID);

    }

  /*  private void makeMove(Integer gameID, String authToken, Session session) throws IOException, DataAccessException {
        String username = authDataAccess.getAuth(authToken).username();
        var message = String.format("%s made a move, yeah he made a moooove (Steve Lacy)",username);
        ServerMessage loadGameToAll = new LoadGameMessage(gameDataAccess.getGame(gameID).game());
        ServerMessage loadGameBroadcast = new NotificationMessage(message);
        connections.broadcastToAll(loadGameToAll,gameID);
        connections.broadcastRootClient(session, loadGameBroadcast, gameID);

    }

    public void leave(Integer gameID, String authToken, Session session) throws DataAccessException, IOException {
        String username = authDataAccess.getAuth(authToken).username();
        var message = String.format("%s left the game because he's scared",username);
        ServerMessage loadGameBroadcast = new NotificationMessage(message);
        connections.broadcastExceptRoot(session,loadGameBroadcast,gameID);
        connections.remove(gameID,session);
    }
    private void resign(String visitorName, Session session) throws IOException {
    }*/
}
