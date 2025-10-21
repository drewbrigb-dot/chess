package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

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

}
