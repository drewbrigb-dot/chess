package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import service.GameService;
import service.UserService;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SQLDataTests {

    UserDataAccess dbUser;
    AuthDataAccess dbAuth;
    GameDataAccess dbGame;

    public SQLDataTests() throws DataAccessException {
        dbUser = new SQLUserDataAccess();
        dbAuth = new SQLAuthDataAccess();
        dbGame = new SQLGameDataAccess();
    }

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
    void getUser() throws Exception {
        SQLUserDataAccess sqlUserDataAccess = new SQLUserDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        sqlUserDataAccess.createUser(user);
        assertTrue(BCrypt.checkpw(user.password(), sqlUserDataAccess.getUser(user.username()).password()));
    }

    @Test
    void getBadUser() throws Exception {
        SQLUserDataAccess sqlUserDataAccess = new SQLUserDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        String badPassword = "peepee";
        sqlUserDataAccess.createUser(user);
        assertFalse(BCrypt.checkpw(badPassword, sqlUserDataAccess.getUser(user.username()).password()));
    }

    @Test
    void validateUser()throws Exception {
        dbUser.clear();
        dbAuth.clearAuth();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        dbUser.createUser(user);
        assertTrue(dbUser.validateUser(user.username(),user.password()));
    }

    @Test
    void validateBadUser()throws Exception {
        dbUser.clear();
        dbAuth.clearAuth();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        dbUser.createUser(user);
        String badUsername = "poop";
        assertFalse(dbUser.validateUser(badUsername,user.password()));
    }

    @Test
    void login() throws Exception {
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

    @Test
    void getGame() throws Exception {
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

        assertEquals("ChessIsStupid",dbGame.getGame(gameID).gameName());
    }

    @Test
    void getBadGame() throws Exception {
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userBad = new UserData("tommy", "tommy@tom.com", "toomanyshawties");
        var userService = new UserService(dbUser, dbAuth);
        GameService gameService = new GameService(dbGame,dbAuth);
        AuthData authData = userService.register(user);
        AuthData authBadData = userService.register(userBad);

        String authToken = authData.authToken();
        String authTokenBad = authBadData.authToken();

        Integer gameID = gameService.createGame(authToken, "ChessIsStupid");
        Integer gameBadID = gameService.createGame(authTokenBad, "ChessIsBad");

        assertNotEquals("ChessIsBad",dbGame.getGame(gameID).gameName());
    }



     @Test
    void clear() throws Exception {
        var user = new UserData("", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        userService.register(user);
        assertDoesNotThrow(()->dbUser.clear());
        assertDoesNotThrow(()->dbAuth.clearAuth());
        assertDoesNotThrow(()->dbGame.clearGame());

    }

    @Test
    void createAuth() throws Exception {
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        AuthData userAuth = new AuthData(user.username(),UUID.randomUUID().toString());
        assertDoesNotThrow(()->dbAuth.createAuth(userAuth));
    }

    @Test
    void createBadAuth() throws Exception {
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData(null, "j@j.com", "toomanysecrets");
        AuthData userAuth = new AuthData(user.username(),UUID.randomUUID().toString());
        assertThrows(DataAccessException.class,()->dbAuth.createAuth(userAuth));
    }

    @Test
    void getAuth() throws Exception {
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        AuthData authData = userService.register(user);
        assertEquals(authData,dbAuth.getAuth(authData.authToken()));
    }

    @Test
    void getBadAuth() throws Exception {
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var badUser = new UserData("poo","poo@gmail.com","poopoopeepee");
        var userService = new UserService(dbUser, dbAuth);
        AuthData authData = userService.register(user);
        AuthData authDataBad = userService.register(badUser);
        assertNotEquals(authDataBad,dbAuth.getAuth(authData.authToken()));
    }

    @Test
    void deleteAuth() throws Exception {
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        AuthData authData = userService.register(user);
        dbAuth.deleteAuth(authData.authToken());
        assertNull(dbAuth.getAuth(authData.authToken()));
    }

    @Test
    void deleteBadAuth() throws Exception {
        dbUser.clear();
        dbAuth.clearAuth();
        dbGame.clearGame();

        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        AuthData authData = userService.register(user);
        assertDoesNotThrow(() ->dbAuth.deleteAuth(null));

    }







}
