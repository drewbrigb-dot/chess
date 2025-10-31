package dataaccess;

import model.*;
import org.junit.jupiter.api.function.Executable;

public interface AuthDataAccess {
    void clearAuth() throws DataAccessException;
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    //boolean isEmpty();
    void createDatabase() throws DataAccessException;
}
