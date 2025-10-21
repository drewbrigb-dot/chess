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
    public Integer createGame(String authToken, String gameName) {

        int newGameID;

        if (gameData.isEmpty()) {
            newGameID = 1;
        }else {
            int maxKeyValue = Collections.max(gameData.keySet());
            newGameID = maxKeyValue + 1;
        }

        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(newGameID,"","",gameName,chessGame);

        gameData.put(newGameID,game);
        return newGameID;
    }

    @Override
    public ArrayList<String> listOfGames(String authToken) {
        return null;
    }
}
