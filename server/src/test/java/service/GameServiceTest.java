package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class GameServiceTest {
    @Test
    void createGame() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        GameDataAccess dbGame = new GameMemoryDataAccess();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        GameService gameService = new GameService(dbGame,dbAuth);
        AuthData authData = userService.register(user);

       int gameID = gameService.createGame(authData.authToken(),"ChessIsLame");

        assertEquals("ChessIsLame",gameService.gameDataAccess.getGame(gameID).gameName());
    }
    @Test
    void createInvalidGame() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        GameDataAccess dbGame = new GameMemoryDataAccess();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        GameService gameService = new GameService(dbGame,dbAuth);
        AuthData authData = userService.register(user);

        Exception e = assertThrows(Exception.class, () -> gameService.createGame(authData.authToken(),null));
        assertEquals("Error: bad request", e.getMessage());
    }
    @Test
    void listGames() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        GameDataAccess dbGame = new GameMemoryDataAccess();

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
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        GameDataAccess dbGame = new GameMemoryDataAccess();

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

}
