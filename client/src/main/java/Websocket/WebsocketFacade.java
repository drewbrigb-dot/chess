package Websocket;
import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import jakarta.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebsocketFacade(String url, NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR ) {
                        ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                        notificationHandler.notifyError(errorMessage);
                    }else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        LoadGameMessage loadMessage = new Gson().fromJson(message, LoadGameMessage.class);
                        notificationHandler.notifyLoad(loadMessage);
                    }else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                        NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                        notificationHandler.notifyNotification(notificationMessage);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, Integer gameID) throws Exception {
        try {
            var commandType = new UserGameCommand (UserGameCommand.CommandType.CONNECT, authToken,gameID );
            this.session.getBasicRemote().sendText(new Gson().toJson(commandType));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move) throws Exception {
        try {
            MakeMoveCommand makeMoveCommand = new MakeMoveCommand(authToken,gameID,move);
            this.session.getBasicRemote().sendText(new Gson().toJson(makeMoveCommand));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void leave(String authToken, Integer gameID) throws Exception {
        try {
            var commandType = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken,gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(commandType));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) throws Exception {
        try {
            var commandType = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken,gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(commandType));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
