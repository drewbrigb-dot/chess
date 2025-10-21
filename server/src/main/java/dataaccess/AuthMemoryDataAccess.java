package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class AuthMemoryDataAccess implements AuthDataAccess {
    private final HashMap<String, AuthData> usersAuth = new HashMap<>();

    @Override
    public void clearAuth() {
        usersAuth.clear();
    }

    @Override
    public void createAuth(AuthData authData) {
        usersAuth.put(authData.authToken(),authData);
    }

    @Override
    public String getAuth(String authToken) {
        return "";
    }

    @Override
    public void deleteAuth(String authToken) {

    }
}
