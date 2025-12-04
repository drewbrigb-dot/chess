package Websocket;


import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public interface NotificationHandler {
    void notifyLoad(LoadGameMessage loadGameMessage);
    void notifyError(ErrorMessage errorMessage);
    void notifyNotification(NotificationMessage notificationMessage);
}
