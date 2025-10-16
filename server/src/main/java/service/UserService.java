package service;

import dataaccess.DataAccess;
import model.*;

public class UserService {
    private final DataAccess dataAccess;
    public UserService (DataAccess dataAccess) {
        this.dataAccess=dataAccess;
    }
    public AuthData register(UserData user) {
        if (dataAccess.getUser(user.username()) != null){
            throw new Exception("already exists");
        }
        dataAccess.createUser(user);
        return new AuthData(user.username(),generateAuthToken());
    }

    private String generateAuthToken () {
        return "xyz";
    }
}
