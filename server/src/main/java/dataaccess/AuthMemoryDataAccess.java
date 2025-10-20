package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class AuthMemoryDataAccess {
    private final HashMap<String, AuthData> usersAuth = new HashMap<>();
}
