package service;

import dataaccess.UserDataAccess;
import dataaccess.UserMemoryDataAccess;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    void register() throws Exception {
        UserDataAccess db = new UserMemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        var authData = userService.register(user);
        assertNotNull(authData);
        assertEquals(user.username(),authData.username());
        assertTrue(!authData.authToken().isEmpty());

    }
    @Test
    void registerInvalidUsername() throws Exception {
        UserDataAccess db = new UserMemoryDataAccess();
        var user = new UserData("", "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        var authData = userService.register(user);
        assertNotNull(authData);
        assertEquals(user.username(),authData.username());
        assertTrue(!authData.authToken().isEmpty());

    }
    @Test
    void clear() throws Exception{
        UserDataAccess db = new UserMemoryDataAccess();
        var user = new UserData("", "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        var authData = userService.register(user);

        db.clear();

    }
}