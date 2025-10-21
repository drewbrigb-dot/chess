package service;

import dataaccess.AuthDataAccess;
import dataaccess.AuthMemoryDataAccess;
import dataaccess.UserDataAccess;
import dataaccess.UserMemoryDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void register() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        var authData = userService.register(user);
        assertNotNull(authData);
        assertEquals(user.username(),authData.username());
        assertTrue(!authData.authToken().isEmpty());

    }
    @Test
    void login() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser,dbAuth);
        userService.register(user);

        AuthData authData = userService.login(user);
        assertEquals("joe",authData.username());
    }
    @Test
    void logout() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser, dbAuth);
        AuthData authData = userService.register(user);

        userService.login(user);
        userService.logout(authData.authToken());
        assertEquals(true, userService.isAuthDataEmpty());
    }

    @Test
    void logoutInvalidToken() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser,dbAuth);
        AuthData authData = userService.register(user);

        String randAuthToken = UUID.randomUUID().toString();
        userService.login(user);
        Exception e = assertThrows(Exception.class, () -> userService.logout(randAuthToken)) ;
        assertEquals("Error: unauthorized",e.getMessage());

    }


    @Test
    void loginWrongUsername() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userWrong = new UserData("joeschmo", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser,dbAuth);
        userService.register(user);

        Exception e = assertThrows(Exception.class, () -> userService.login(userWrong));
        assertEquals("Error: unauthorized",e.getMessage());
    }

    @Test
    void loginNullPassword() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userWrong = new UserData("joe", "j@j.com",null);
        var userService = new UserService(dbUser,dbAuth);
        userService.register(user);

        Exception e = assertThrows(Exception.class, () -> userService.login(userWrong));
        assertEquals("Error: bad request",e.getMessage());
    }
    @Test
    void registerNullUsername() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        var user = new UserData(null, "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser,dbAuth);
        Exception e = assertThrows(Exception.class, () -> userService.register(user));
        assertEquals("Error: bad request", e.getMessage());

    }
    @Test
    void registerInvalidUsername() throws Exception {
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userDuplicate = new UserData("joe", "j@j.gov", "notmanysecrets");
        var userService = new UserService(dbUser,dbAuth);
        userService.register(user);
        Exception e = assertThrows(Exception.class, () -> userService.register(userDuplicate));
        assertEquals("Error: already exists", e.getMessage());

    }
    @Test
    void clear() throws Exception{
        UserDataAccess dbUser = new UserMemoryDataAccess();
        AuthDataAccess dbAuth = new AuthMemoryDataAccess();
        var user = new UserData("", "j@j.com", "toomanysecrets");
        var userService = new UserService(dbUser,dbAuth);
        userService.register(user);
        dbUser.clear();
        dbAuth.clearAuth();

        assertTrue(userService.getAuthData().isEmpty());

    }
}