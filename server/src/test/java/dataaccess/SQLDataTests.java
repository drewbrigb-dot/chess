package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SQLDataTests {
    @Test
    public void createUserDatabase() throws Exception {
        SQLUserDataAccess sqlUserDataAccess = new SQLUserDataAccess();
        assertDoesNotThrow(sqlUserDataAccess::createDatabase);
    }

    @Test
    public void createAuthDatabase() throws Exception {
        SQLAuthDataAccess sqlAuthDataAccess = new SQLAuthDataAccess();
        assertDoesNotThrow(sqlAuthDataAccess::createDatabase);
    }
    @Test
    public void createGameDatabase() throws Exception {
        SQLGameDataAccess sqlGameDataAccess = new SQLGameDataAccess();
        assertDoesNotThrow(sqlGameDataAccess::createDatabase);
    }

    @Test
    void createUser() throws Exception {
        SQLUserDataAccess sqlUserDataAccess = new SQLUserDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        sqlUserDataAccess.createUser(user);
        assertEquals(user,sqlUserDataAccess.getUser(user.username()));

    }

    @Test
    void createBadUser() throws Exception {
        SQLUserDataAccess sqlUserDataAccess = new SQLUserDataAccess();
        var user = new UserData(null, "j@j.com", "toomanysecrets");


        Exception e = assertThrows(Exception.class, () -> sqlUserDataAccess.createUser(user)) ;
        assertEquals("No parameter can be null",e.getMessage());

    }
    @Test
    void login() throws Exception {
        UserDataAccess dbUser = new SQLUserDataAccess();
        AuthDataAccess dbAuth = new SQLAuthDataAccess();
        dbUser.clear();
        dbAuth.clearAuth();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser,dbAuth);
        userService.register(user);

        AuthData authData = userService.login(user);
        assertEquals("joe",authData.username());
    }

    @Test
    void loginWrongUsername() throws Exception {
        UserDataAccess dbUser = new SQLUserDataAccess();
        AuthDataAccess dbAuth = new SQLAuthDataAccess();
        dbUser.clear();
        dbAuth.clearAuth();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userWrong = new UserData("joeschmo", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser,dbAuth);
        userService.register(user);

        Exception e = assertThrows(Exception.class, () -> userService.login(userWrong));
        assertEquals("Error: unauthorized",e.getMessage());
    }

    @Test
    void logout() throws Exception {
        UserDataAccess dbUser = new SQLUserDataAccess();
        AuthDataAccess dbAuth = new SQLAuthDataAccess();
        dbUser.clear();
        dbAuth.clearAuth();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        AuthData authData = userService.register(user);

        userService.login(user);
        userService.logout(authData.authToken());
        assertEquals(user, dbUser.getUser(user.username()));
    }

    @Test
    void logoutInvalidToken() throws Exception {
        UserDataAccess dbUser = new SQLUserDataAccess();
        AuthDataAccess dbAuth = new SQLAuthDataAccess();
        dbUser.clear();
        dbAuth.clearAuth();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser,dbAuth);
        AuthData authData = userService.register(user);

        String randAuthToken = UUID.randomUUID().toString();
        userService.login(user);
        Exception e = assertThrows(Exception.class, () -> userService.logout(randAuthToken)) ;
        assertEquals("Error: unauthorized",e.getMessage());

    }

    @Test
    void createGame() throws Exception {
        UserDataAccess dbUser = new SQLUserDataAccess();
        AuthDataAccess dbAuth = new SQLAuthDataAccess();
        GameDataAccess dbGame = new SQLGameDataAccess();
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        GameService gameService = new GameService(dbGame,dbAuth);
        AuthData authData = userService.register(user);

        int gameID = gameService.createGame(authData.authToken(),"ChessIsLame");

        assertEquals("ChessIsLame", gameService.gameDataAccess.getGame(gameID).gameName());
    }

    @Test
    void createInvalidGame() throws Exception {
        UserDataAccess dbUser = new SQLUserDataAccess();
        AuthDataAccess dbAuth = new SQLAuthDataAccess();
        GameDataAccess dbGame = new SQLGameDataAccess();
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        GameService gameService = new GameService(dbGame,dbAuth);
        AuthData authData = userService.register(user);

        Exception e = assertThrows(Exception.class, () -> gameService.createGame(authData.authToken(),null));
        assertEquals("Error: bad request", e.getMessage());
    }

    @Test
    void listGames() throws Exception {
        UserDataAccess dbUser = new SQLUserDataAccess();
        AuthDataAccess dbAuth = new SQLAuthDataAccess();
        GameDataAccess dbGame = new SQLGameDataAccess();
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        GameService gameService = new GameService(dbGame,dbAuth);
        AuthData authData = userService.register(user);

        String authToken = authData.authToken();

        gameService.createGame(authToken, "ChessIsStupid");

        ArrayList<GameData> gameList = gameService.listOfGames(authToken);

        assertEquals(dbGame.listOfGames(), gameList);

    }

    @Test
    void listWithBadToken() throws Exception {
        UserDataAccess dbUser = new SQLUserDataAccess();
        AuthDataAccess dbAuth = new SQLAuthDataAccess();
        GameDataAccess dbGame = new SQLGameDataAccess();
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        GameService gameService = new GameService(dbGame,dbAuth);
        AuthData authData = userService.register(user);

        String authToken = authData.authToken();

        gameService.createGame(authToken, "ChessIsStupid");

        String badAuthToken = UUID.randomUUID().toString();

        Exception e = assertThrows(Exception.class, () -> gameService.listOfGames(badAuthToken));
        assertEquals("Error: unauthorized", e.getMessage());

    }
    @Test
    void joinGameSuccess() throws Exception {
        UserDataAccess dbUser = new SQLUserDataAccess();
        AuthDataAccess dbAuth = new SQLAuthDataAccess();
        GameDataAccess dbGame = new SQLGameDataAccess();
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        GameService gameService = new GameService(dbGame,dbAuth);
        AuthData authData = userService.register(user);

        String authToken = authData.authToken();

        Integer gameID = gameService.createGame(authToken, "ChessIsStupid");

        gameService.joinGame(ChessGame.TeamColor.WHITE,gameID,user.username());

        assertEquals("joe",dbGame.getGame(gameID).whiteUsername());
    }

    @Test
    void joinGameFailure() throws Exception {
        UserDataAccess dbUser = new SQLUserDataAccess();
        AuthDataAccess dbAuth = new SQLAuthDataAccess();
        GameDataAccess dbGame = new SQLGameDataAccess();
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userBad = new UserData("tommy", "tommy@tom.com", "toomanyshawties");
        var userService = new UserService(dbUser, dbAuth);
        GameService gameService = new GameService(dbGame,dbAuth);
        AuthData authData = userService.register(user);

        String authToken = authData.authToken();

        Integer gameID = gameService.createGame(authToken, "ChessIsStupid");

        gameService.joinGame(ChessGame.TeamColor.WHITE,gameID,user.username());

        Exception e = assertThrows(Exception.class, () -> gameService.joinGame(ChessGame.TeamColor.WHITE,gameID,"tommy"));
        assertEquals("Error: already taken", e.getMessage());



    }

}
