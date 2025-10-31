package dataaccess;

import model.*;
import org.junit.jupiter.api.function.Executable;

public interface UserDataAccess {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    boolean validateUser(String username, String password) throws DataAccessException;
    void createDatabase() throws DataAccessException;
}
