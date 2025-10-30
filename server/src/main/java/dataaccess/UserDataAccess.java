package dataaccess;

import model.*;
public interface UserDataAccess {
    void clear() throws Exception;
    void createUser(UserData user) throws Exception;
    UserData getUser(String username) throws Exception;
    boolean isEmpty();
    boolean validateUser(String username, String password);
    void createDatabase() throws Exception;
}
