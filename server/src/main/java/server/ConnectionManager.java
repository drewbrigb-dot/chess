
package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();

    public void add(Integer gameID,Session session) {

        if(connections.get(gameID) == null) {
            Set<Session> sessions = new HashSet<>();
            sessions.add(session);
            connections.put(gameID, sessions);
        }else{
            Set<Session> sessions = connections.get(gameID);
            sessions.add(session);
            connections.put(gameID, sessions);
        }
    }
    public void remove(Integer gameID,Session session) {
        Set<Session> sessions = connections.get(gameID);
        sessions.remove(session);
        connections.remove(gameID);
        connections.put(gameID, sessions);
    }

    public void broadcastExceptRoot(Session excludeSession, ServerMessage serverMessage, Integer gameID) throws IOException {
        if(connections.get(gameID) == null) {return;}
        var serializer = new Gson();
        String msg = serializer.toJson(serverMessage);
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void broadcastRootClient(Session excludeSession, ServerMessage serverMessage,Integer gameID) throws IOException {
        if(connections.get(gameID) == null) {return;}
        var serializer = new Gson();
        String msg = serializer.toJson(serverMessage);
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                if (c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

    public void broadcastErrorToRoot(Session excludeSession, ServerMessage serverMessage) throws IOException {
        var serializer = new Gson();
        String msg = serializer.toJson(serverMessage);
        for (Set<Session> sessions : connections.values()) {
            for (Session session : sessions) {
                if (session.isOpen()) {
                    if (session.equals(excludeSession)) {
                        session.getRemote().sendString(msg);
                    }
                }
            }
        }
    }
    public void broadcastToAll(ServerMessage serverMessage,Integer gameID) throws IOException {
        if(connections.get(gameID) == null) {return;}
        var serializer = new Gson();
        String msg = serializer.toJson(serverMessage);
        for (Session c : connections.get(gameID)) {
            if (c.isOpen()) {
                c.getRemote().sendString(msg);
            }
        }
    }
}

