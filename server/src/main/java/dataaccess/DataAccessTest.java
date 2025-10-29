/*
package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {

    @Test
    void clear() {
        UserDataAccess db = new UserMemoryDataAccess();
        db.createUser(new UserData("joe", "j@j.com", "toomanysecrets"));
        db.clear();
        assertNull(db.getUser("joe"));
    }

    @Test
    void createUser() {
        UserDataAccess db = new UserMemoryDataAccess();
        var user = new UserData("joe", "j@j.com", "toomanysecrets");
        db.createUser(user);
        assertEquals(user, db.getUser(user.username()));
    }

    @Test
    void getUser() {
    }
}*/
