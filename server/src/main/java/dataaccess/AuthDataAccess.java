package dataaccess;

public interface AuthDataAccess {
    void createAuth();
    String getAuth(String authToken);
    void deleteAuth(String authToken);
}
