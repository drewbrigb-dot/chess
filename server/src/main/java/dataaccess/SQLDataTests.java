package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.UserService;

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

}
