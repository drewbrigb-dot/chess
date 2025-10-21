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
    void registerNullUsername() throws Exception {
        UserDataAccess db = new UserMemoryDataAccess();
        var user = new UserData(null, "j@j.com", "toomanysecrets");
        var userService = new UserService(db);
        Exception e = assertThrows(Exception.class, () -> userService.register(user));
        assertEquals("Error: bad request", e.getMessage());

    }
    @Test
    void registerInvalidUsername() throws Exception {
        UserDataAccess db = new UserMemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        var userDuplicate = new UserData("joe", "j@j.gov", "notmanysecrets");
        var userService = new UserService(db);
        userService.register(user);
        Exception e = assertThrows(Exception.class, () -> userService.register(userDuplicate));
        assertEquals("Error: already exists", e.getMessage());

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