package dataaccess;
import chess.ChessGame;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface GameDataAccess  {
    GameData getGame (int gameID) throws DataAccessException;
    Integer createGame(String gameName) throws DataAccessException;
    ArrayList<GameData> listOfGames() throws DataAccessException;
    void clearGame() throws DataAccessException;
    void joinGame(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException;
    void createDatabase() throws DataAccessException;

    //update Game??





}
