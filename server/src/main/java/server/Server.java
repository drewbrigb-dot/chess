package server;

import dataaccess.UserMemoryDataAccess;
import model.*;
import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;



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

    }

    private void register (Context ctx) {
        try {
            var serializer = new Gson();
            String reqJson = ctx.body();
            var user = serializer.fromJson(reqJson, UserData.class);

            //call to the service and register
            AuthData authData = userService.register(user);
            ctx.status(200);
            ctx.result(serializer.toJson(authData));

        }catch (Exception ex) {
            if (ex.getMessage().equals("Error: already exists")) {
                var msg = String.format("Error: username already taken");
                ctx.status(403).result(msg);
            }
            else if (ex.getMessage().equals("Error: bad request")) {
                var msg = String.format("Error: bad request");
                ctx.status(400).result(msg);
            } else {
                var msg = String.format("Error: welp");
                ctx.status(500).result(msg);
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
