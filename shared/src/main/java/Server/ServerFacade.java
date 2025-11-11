package Server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;


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
        if (status == 200) {
            var body = response.body();
            if (body != null) {
                //throw something but idk what
            }
            //throw some exception about failure?
        }
        if(responseClass != null) {
            return new Gson().fromJson(response.body(),responseClass);
        }
        return null;
    }



}
