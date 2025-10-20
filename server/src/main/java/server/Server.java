package server;

import dataaccess.MemoryDataAccess;
import model.*;
import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import service.UserService;



public class Server {

    private final Javalin server;
    private final UserService userService;

    public Server() {
        var dataAccess = new MemoryDataAccess();
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
            var req = serializer.fromJson(reqJson, UserData.class);

            //call to the service and register
            //userService.register(user);


            //ctx.result(serializer.toJson(authData));
        }catch (Exception ex) {
            var msg = String.format("Error: username already taken");
            ctx.status(403).result(msg);
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
