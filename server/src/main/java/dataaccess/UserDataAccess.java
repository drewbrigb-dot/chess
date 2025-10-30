package dataaccess;

import model.*;
public interface UserDataAccess {
    void clear() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    boolean validateUser(String username, String password) throws DataAccessException;
    void createDatabase() throws DataAccessException;
}
