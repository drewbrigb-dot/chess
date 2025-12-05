package websocket;


import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {
    void notifyLoad(LoadGameMessage loadGameMessage);
    void notifyError(ErrorMessage errorMessage);
    void notifyNotification(NotificationMessage notificationMessage);
}
