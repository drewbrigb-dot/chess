package server;

import dataaccess.UserMemoryDataAccess;
import model.*;
import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;

import java.util.Map;


public class Server {

    private final Javalin server;
    private final UserService userService;

    public Server() {
        var dataAccess = new UserMemoryDataAccess();
        //instances of db classes?
        userService = new UserService(dataAccess);
        server = Javalin.create(config -> config.staticFiles.add("web"));

        server.delete("db",ctx -> ctx.result("{}"));
        //endpoints
        server.post("user", ctx->register(ctx));
        server.post("session", ctx->login(ctx));
        server.delete("session", ctx->logout(ctx));

    }

    private void login (Context ctx ) {
        var serializer = new Gson();
        try {
            String reqJson = ctx.body();
            var user = serializer.fromJson(reqJson, UserData.class);

            //call to the service and register
            AuthData authData = userService.login(user);

            ctx.status(200);
            ctx.result(serializer.toJson(authData));

        }catch (Exception ex) {
            if (ex.getMessage().equals("Error: unauthorized")) {
                var msg = Map.of("message","Error: unauthorized");
                ctx.status(401).result(serializer.toJson(msg));
            }
            else if (ex.getMessage().equals("Error: bad request")) {
                var msg = Map.of("message","Error: bad request");
                ctx.status(400).result(serializer.toJson(msg));
            } else {
                var msg = Map.of("message","Error: welp");
                ctx.status(500).result(serializer.toJson(msg));
            }
        }
    }

    private void logout (Context ctx ) {
        var serializer = new Gson();
        try {
            String reqJson;

            String authToken = ctx.header("authorization");

            //call to the service and register
            userService.logout(authToken);

            ctx.status(200);
            ctx.result("{}");

        }catch (Exception ex) {
            if (ex.getMessage().equals("Error: unauthorized")) {
                var msg = Map.of("message","Error: unauthorized");
                ctx.status(401).result(serializer.toJson(msg));
            }
            else {
                var msg = Map.of("message","Error: welp");
                ctx.status(500).result(serializer.toJson(msg));
            }
        }
    }

    private void register (Context ctx) {
        var serializer = new Gson();
        try {
            String reqJson = ctx.body();
            var user = serializer.fromJson(reqJson, UserData.class);

            //call to the service and register
            AuthData authData = userService.register(user);
            ctx.status(200);
            ctx.result(serializer.toJson(authData));

        }catch (Exception ex) {
            if (ex.getMessage().equals("Error: already exists")) {
                var msg = Map.of("message", "Error: username already taken");
                ctx.status(403).result(serializer.toJson(msg));
            }
            else if (ex.getMessage().equals("Error: bad request")) {
                var msg = Map.of("message","Error: bad request");
                ctx.status(400).result(serializer.toJson(msg));
            } else {
                var msg = Map.of("message","Error: welp");
                ctx.status(500).result(serializer.toJson(msg));
            }
        }

    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
