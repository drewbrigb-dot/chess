package dataaccess;
import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;

public interface GameDataAccess  {
    GameData getGame (int gameID) throws DataAccessException;
    Integer createGame(String gameName) throws DataAccessException;
    ArrayList<GameData> listOfGames() throws DataAccessException;
    void clearGame() throws DataAccessException;
    void joinGame(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException;
    void createDatabase() throws DataAccessException;

    //update Game??





}
