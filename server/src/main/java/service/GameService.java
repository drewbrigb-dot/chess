package service;

import chess.ChessGame;
import dataaccess.AuthDataAccess;
import dataaccess.AuthMemoryDataAccess;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;


import model.*;

import java.util.ArrayList;
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

    public ArrayList<GameData> listOfGames(String authToken) throws Exception {
        if (authDataAccess.getAuth(authToken) == null) {
            throw new Exception("Error: unauthorized");
        }

        ArrayList<GameData> gameList ;
        gameList = gameDataAccess.listOfGames();


        return gameList;

    }
    public void joinGame (ChessGame.TeamColor playerColor, Integer gameID, String username)  throws Exception{
        GameData game = gameDataAccess.getGame(gameID);


        if (playerColor == null || username == null) {
            throw new Exception("Error: bad request");
        }
        if (game == null) {
            throw new Exception("Error: unauthorized");
        }
        if (playerColor == ChessGame.TeamColor.WHITE)  {
            if (game.whiteUsername() != null && !game.whiteUsername().isEmpty()) {
                throw new Exception("Error: already taken");
            }
        } else if (game.blackUsername() != null && !game.blackUsername().isEmpty()) {
            throw new Exception("Error: already taken");
        }

        gameDataAccess.joinGame(playerColor,gameID, username);
    }
}
