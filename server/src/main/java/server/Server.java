package server;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.Map;


public class Server {

    private final Javalin server;
    private final UserService userService;
    private final GameService gameService;
    UserMemoryDataAccess userDataAccess;
    GameMemoryDataAccess gameDataAccess;
    AuthDataAccess authDataAccess;

    public Server()throws DataAccessException {
        var userDataAccessSQL = new SQLUserDataAccess();
        var gameDataAccessSQL = new SQLGameDataAccess();
        var authDataAccessSQL = new SQLAuthDataAccess();
        userDataAccess= new UserMemoryDataAccess();
        gameDataAccess = new GameMemoryDataAccess();
        authDataAccess = new AuthMemoryDataAccess();

        //instances of db classes?
        userService = new UserService(userDataAccess,authDataAccess);
        gameService = new GameService(gameDataAccess,authDataAccess);
        server = Javalin.create(config -> config.staticFiles.add("web"));

        //server.delete("db",ctx -> ctx.result("{}"));
        //endpoints
        server.post("user", ctx->register(ctx));
        server.post("session", ctx->login(ctx));
        server.delete("session", ctx->logout(ctx));
        server.post("game", ctx-> createGame(ctx));
        server.delete("db",ctx->clear(ctx));
        server.get("game", ctx-> listGames(ctx));
        server.put("game", ctx->joinGame(ctx));

    }

    private void clear(Context ctx) {
        userDataAccess.clear();
        gameDataAccess.clearGame();
        authDataAccess.clearAuth();

        ctx.status(200);
        ctx.result("{}");

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
            exceptionHelper(ex,ctx,serializer);
        }
    }

    private void logout (Context ctx ) {
        var serializer = new Gson();
        try {


            String authToken = ctx.header("authorization");

            //call to the service and register
            userService.logout(authToken);

            ctx.status(200);
            ctx.result("{}");

        }catch (Exception ex) {
            exceptionHelperTwoEx( ex,  ctx,  serializer);
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
    private void createGame (Context ctx ) {
        var serializer = new Gson();
        try {
            String reqJson = ctx.body();
            String authToken = ctx.header("authorization");

            CreateRequest request = serializer.fromJson(reqJson, CreateRequest.class);
            String gameName = request.gameName();
            //call to the service and register
            Integer gameID = gameService.createGame(authToken,gameName);

            CreateResult gameResult = new CreateResult(gameID);
            ctx.status(200);
            ctx.result(serializer.toJson(gameResult));

        }catch (Exception ex) {
            exceptionHelper(ex,ctx,serializer);
        }
    }

    private void listGames(Context ctx) {
        var serializer = new Gson();
        try {
            String authToken = ctx.header("authorization");

            ArrayList<GameData> gameList = gameService.listOfGames(authToken);
            ArrayList<GameInfo> gameInfoList = new ArrayList<>();

            for (GameData game: gameList) {
                GameInfo gameInfo = new GameInfo(game.gameID(),game.whiteUsername(),game.blackUsername(),game.gameName());
                gameInfoList.add(gameInfo);
            }

            ListGamesResult listGamesResult = new ListGamesResult(gameInfoList);

            ctx.status(200);
            ctx.result(serializer.toJson(listGamesResult));

        }catch (Exception ex) {
            exceptionHelperTwoEx ( ex,  ctx,  serializer);
        }
    }

    private void joinGame (Context ctx ) {
        Gson serializer = new Gson();
        try {
            String reqJson = ctx.body();
            String authToken = ctx.header("authorization");

            JoinGameRequest request = serializer.fromJson(reqJson, JoinGameRequest.class);
            ChessGame.TeamColor playerColor = request.playerColor();
            Integer gameID = request.gameID();


            if (gameID == null) {
                throw new Exception("Error: bad request");
            }
            if (authDataAccess.getAuth(authToken) == null) {
                throw new Exception("Error: unauthorized");
            }
            String username = authDataAccess.getAuth(authToken).username();
            gameService.joinGame(playerColor,gameID, username);

            ctx.status(200);
            ctx.result("{}");

        }catch (Exception ex) {
            if (ex.getMessage().equals("Error: unauthorized")) {
                var msg = Map.of("message", "Error: unauthorized");
                ctx.status(401).result(serializer.toJson(msg));
            }
            else if (ex.getMessage().equals("Error: bad request")) {
                var msg = Map.of("message","Error: bad request");
                ctx.status(400).result(serializer.toJson(msg));
            }else if (ex.getMessage().equals("Error: already taken")) {
                var msg = Map.of("message", "Error: already taken");
                ctx.status(403).result(serializer.toJson(msg));
            } else {
                var msg = Map.of("message","Error: welp");
                ctx.status(500).result(serializer.toJson(msg));
            }
        }

    }

    public void exceptionHelper (Exception ex, Context ctx, Gson serializer){
        if (ex.getMessage().equals("Error: unauthorized")) {
            var msg = Map.of("message", "Error: unauthorized");
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
    public void exceptionHelperTwoEx (Exception ex, Context ctx, Gson serializer){
        if (ex.getMessage().equals("Error: unauthorized")) {
            var msg = Map.of("message", "Error: unauthorized");
            ctx.status(401).result(serializer.toJson(msg));
        } else {
            var msg = Map.of("message","Error: welp");
            ctx.status(500).result(serializer.toJson(msg));
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
