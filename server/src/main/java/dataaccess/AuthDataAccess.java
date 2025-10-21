package dataaccess;

import model.*;

public interface AuthDataAccess {
    void clearAuth();
    void createAuth(AuthData authData);
    String getAuth(String authToken);
    void deleteAuth(String authToken);
}
