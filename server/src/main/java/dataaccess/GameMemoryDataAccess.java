package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class GameMemoryDataAccess implements GameDataAccess {
    private final HashMap<Integer, GameData> gameData = new HashMap<>();

    @Override
    public void clearGame() {
        gameData.clear();
    }

    @Override
    public GameData getGame(int gameID) {
        return gameData.get(gameID);
    }

    @Override
    public Integer createGame(String gameName) {

        int newGameID;

        if (gameData.isEmpty()) {
            newGameID = 1;
        }else {
            int maxKeyValue = Collections.max(gameData.keySet());
            newGameID = maxKeyValue + 1;
        }

        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(newGameID,null,null,gameName,chessGame);

        gameData.put(newGameID,game);
        return newGameID;
    }

    @Override
    public ArrayList<GameData> listOfGames() {
        ArrayList<GameData> gameList = new ArrayList<>(gameData.values());
        return gameList;
    }

    @Override
    public void joinGame(ChessGame.TeamColor playerColor, Integer gameID, String newUsername) {
        GameData oldGame = gameData.get(gameID);
        String whiteUsername = oldGame.whiteUsername();
        String blackUsername = oldGame.blackUsername();

        GameData newGame;
        if (playerColor == ChessGame.TeamColor.BLACK) {
            newGame = new GameData(gameID, whiteUsername, newUsername, oldGame.gameName(), oldGame.game());
        }else {
            newGame = new GameData(gameID, newUsername, blackUsername, oldGame.gameName(), oldGame.game());
        }
        gameData.remove(gameID);
        gameData.put(gameID,newGame);

    }

    @Override
    public void createDatabase() throws DataAccessException {
        System.console().printf("You're not supposed to be here");
    }
}
