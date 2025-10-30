package dataaccess;

import model.*;
public interface UserDataAccess {
    void clear() throws Exception;
    void createUser(UserData user) throws Exception;
    UserData getUser(String username) throws Exception;
    boolean validateUser(String username, String password) throws Exception;
    void createDatabase() throws Exception;
}
