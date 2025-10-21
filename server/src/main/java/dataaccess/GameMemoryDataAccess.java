package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMemoryDataAccess implements GameDataAccess {
    private final HashMap<Integer, GameData> gameData = new HashMap<>();

    @Override
    public void clearGame() {
        gameData.clear();
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void createGame(String authToken, String gameName) {

    }

    @Override
    public ArrayList<String> listOfGames(String authToken) {
        return null;
    }
}
