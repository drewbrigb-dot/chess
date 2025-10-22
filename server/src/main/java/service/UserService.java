package service;

import dataaccess.AuthDataAccess;
import dataaccess.AuthMemoryDataAccess;
import dataaccess.UserDataAccess;


import model.*;

import java.util.UUID;

public class UserService {
    private final UserDataAccess userData;
    private final AuthDataAccess authData;
    public UserService (UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        userData = userDataAccess;
        authData = authDataAccess;

    }
    public AuthData register(UserData user) throws Exception {
        if (user.username() == null || user.password() == null) {
            throw new Exception("Error: bad request");
        }
        if (userData.getUser(user.username()) != null){
            throw new Exception("Error: already exists");
        }

        userData.createUser(user);
        AuthData userAuth = new AuthData(user.username(),generateAuthToken());
        authData.createAuth(userAuth);

        return userAuth;
    }

    public AuthData login (UserData user) throws Exception {
        if (user.username() == null || user.password() == null) {
            throw new Exception("Error: bad request");
        }
        String username = user.username();
        String password = user.password();

        if (!userData.validateUser(username, password)) {
            throw new Exception("Error: unauthorized");
        }
        AuthData auth = new AuthData(username, generateAuthToken());
        authData.createAuth(auth);

        return auth;
    }

    public boolean logout (String authToken) throws Exception {
        if (authToken == null) {
            throw new Exception("Error: bad request");
        }
        if (authData.getAuth(authToken) == null) {
            throw new Exception("Error: unauthorized");
        }

        authData.deleteAuth(authToken);
        return true;
    }

    private String generateAuthToken () {
        return UUID.randomUUID().toString();
    }

}
