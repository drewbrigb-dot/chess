package dataaccess;
import chess.ChessGame;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public interface GameDataAccess  {
    GameData getGame (int gameID);
    Integer createGame(String authToken, String gameName);
    ArrayList<GameData> listOfGames();
    void clearGame();
    void joinGame(ChessGame.TeamColor playerColor, Integer gameID, String username);

    //update Game??





}
