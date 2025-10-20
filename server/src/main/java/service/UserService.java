package service;

import dataaccess.UserDataAccess;

import model.*;

import java.util.UUID;

public class UserService {
    private final UserDataAccess dataAccess;
    public UserService (UserDataAccess dataAccess) {
        this.dataAccess=dataAccess;
    }
    public AuthData register(UserData user) throws Exception {
        if (dataAccess.getUser(user.username()) != null){
            throw new Exception("already exists");
        }
        dataAccess.createUser(user);
        return new AuthData(user.username(),generateAuthToken());
    }

    private String generateAuthToken () {
        return UUID.randomUUID().toString();
    }
}
