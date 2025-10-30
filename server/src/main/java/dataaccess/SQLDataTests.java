package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.UserService;

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

}
