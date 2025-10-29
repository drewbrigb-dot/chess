package dataaccess;

import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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

}
