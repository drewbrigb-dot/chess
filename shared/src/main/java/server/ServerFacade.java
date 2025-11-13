package server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.*;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;


public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {serverUrl = url;}
    //make sure where to find url

    public AuthData register (UserData userData) throws Exception {
        var request = buildRequest("Post","/user",userData);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public AuthData login (UserData userData) throws Exception {
        var request = buildRequest("Post","/session",userData);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public void logout (String authToken) throws Exception {
        HttpRequest request = buildRequestHeader("Delete","/session",null,"authorization",authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public Integer createGame (String authToken, String gameName) throws Exception{
        CreateRequest createRequest = new CreateRequest(gameName);
        HttpRequest request = buildRequestHeader("Post","/game",createRequest,"authorization",authToken);
        var response = sendRequest(request);
        CreateResult createResult=  handleResponse(response, CreateResult.class);
        return createResult.gameID();

    }

    public void joinGame (ChessGame.TeamColor color, Integer gameID, String authToken)  throws Exception{
        JoinGameRequest joinGameRequest = new JoinGameRequest(color, gameID);
        var request = buildRequestHeader("Put","/game",joinGameRequest,"authorization",authToken);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    public ArrayList<GameInfo> listGames (String authToken) throws Exception {
        var request = buildRequestHeader("Get","/game",null,"authorization",authToken);
        var response = sendRequest(request);
        ListGamesResult listGamesResult = handleResponse(response, ListGamesResult.class);
        ArrayList<GameInfo> gameList = listGamesResult.games();
        return gameList;
    }


    public void clear()throws Exception {
        var request = buildRequest("DELETE", "/db", null);
        sendRequest(request);
    }

    private HttpRequest buildRequestHeader (String method, String path, Object body, String headerName, String headerValue) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .header(headerName,headerValue)
                .method(method,makeRequestBody(body));

        return request.build();
    }

    private HttpRequest buildRequest (String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method,makeRequestBody(body));
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }
    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception{
        try {
            return client.send(request, BodyHandlers.ofString());
        }catch (Exception ex) {
            throw new Exception(ex.getMessage());
            //idk what to do here for the exception
        }
    }
    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (status != 200) {
            var body = response.body();
            if (body != null) {
                //throw something but idk what
                JsonObject obj = JsonParser.parseString(body).getAsJsonObject();
                String errorMessage = obj.get("message").getAsString();
                throw new Exception(errorMessage);
            }
            //throw some exception about failure?
            throw new Exception("other failure" + status);
        }
        if(responseClass != null) {
            return new Gson().fromJson(response.body(),responseClass);
        }
        return null;
    }



}
