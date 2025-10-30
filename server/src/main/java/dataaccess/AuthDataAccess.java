package dataaccess;

import model.*;

public interface AuthDataAccess {
    void clearAuth();
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    //boolean isEmpty();
    void createDatabase() throws DataAccessException;
}
