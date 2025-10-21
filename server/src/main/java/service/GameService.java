package service;

import dataaccess.AuthDataAccess;
import dataaccess.AuthMemoryDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;


import model.*;

import java.util.UUID;

public class GameService {
    GameDataAccess gameDataAccess;
    AuthDataAccess authDataAccess;
    public GameService (GameDataAccess gameDAO, AuthDataAccess authDAO) {
        gameDataAccess = gameDAO;
        authDataAccess = authDAO;
    }

    public Integer createGame (String authToken, String gameName) throws Exception {
        if (authToken == null || gameName == null) {
            throw new Exception("Error: bad request");
        }

        AuthData auth = authDataAccess.getAuth(authToken);
        if (auth == null) {
            throw new Exception("Error: unauthorized");
        }

        Integer gameID = gameDataAccess.createGame(authToken,gameName);

        return gameID;

    }
}
