package ui;

import java.net.http.HttpClient;

public class ServerFacade {
        public RegisterResult register(RegisterRequest request) {...}
        public LoginResult login(LoginRequest request) {}
        public JoinResult joinGame (JoinRequest request){}

    private final HttpClient client = HttpClient.newHttpClient();
}
