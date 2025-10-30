package dataaccess;

import model.*;

public interface AuthDataAccess {
    void clearAuth();
    void createAuth(AuthData authData) throws Exception;
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
    //boolean isEmpty();
    void createDatabase() throws Exception;
}
