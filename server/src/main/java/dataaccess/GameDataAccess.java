package dataaccess;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public interface GameDataAccess  {
    GameData getGame (int gameID);
    void createGame(String authToken, String gameName);
    ArrayList<String> listOfGames(String authToken);
    void clearGame();

    //update Game??





}
