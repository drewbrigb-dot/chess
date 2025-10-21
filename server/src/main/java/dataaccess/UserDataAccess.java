package dataaccess;

import model.*;
public interface UserDataAccess {
    void clear();
    void createUser(UserData user);
    UserData getUser(String username);
    boolean isEmpty();
    boolean validateUser(String username, String password);
}
