package service;

import dataaccess.AuthDataAccess;
import dataaccess.AuthMemoryDataAccess;
import dataaccess.UserDataAccess;


import model.*;

import java.util.UUID;

public class UserService {
    private final UserDataAccess userData;
    private final AuthDataAccess authData;
    public UserService (UserDataAccess userData) {
        this.userData=userData;
        authData = new AuthMemoryDataAccess();

    }
    public AuthData register(UserData user) throws Exception {
        if (userData.getUser(user.username()) != null){
            throw new Exception("Error: already exists");
        }
        if (user.username() == null) {
            throw new Exception("Error: bad request");
        }
        userData.createUser(user);
        AuthData userAuth = new AuthData(user.username(),generateAuthToken());
        authData.createAuth(userAuth);

        return userAuth;
    }

    private String generateAuthToken () {
        return UUID.randomUUID().toString();
    }
}
