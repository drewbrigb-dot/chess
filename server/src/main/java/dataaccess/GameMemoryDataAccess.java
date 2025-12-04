package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.GameInfo;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
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
    public void updateGame(ChessGame game, Integer gameID) {
       GameData gameDataInfo = gameData.get(gameID);
       String whiteUsername = gameDataInfo.whiteUsername();
       String blackUsername = gameDataInfo.blackUsername();
       String gameName = gameDataInfo.gameName();
       GameData newGame = new GameData(gameID,whiteUsername,blackUsername,gameName,game);
       gameData.put(gameID,newGame);
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
    public void updateUsernames (String whiteUsername, String blackUsername, Integer gameID) {
        GameData gameDataLocal = gameData.get(gameID);
        String gameName = gameDataLocal.gameName();
        ChessGame chessGame = gameDataLocal.game();
        GameData newGameData = new GameData(gameID,whiteUsername,blackUsername,gameName,chessGame);
        gameData.put(gameID,newGameData);
    }

    @Override
    public void createDatabase() throws DataAccessException {
        System.console().printf("You're not supposed to be here");
    }
}
