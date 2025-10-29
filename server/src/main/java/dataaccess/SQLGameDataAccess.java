package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDataAccess implements GameDataAccess{
    public SQLGameDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
    }


    public void createDatabase() throws Exception {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatement) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex ) {
            throw new Exception("Unable to read data");
        }
    }
    //make create tables function
    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
               id INT NOT NULL AUTO_INCREMENT,
              `gameID` INT NOT NULL,
              `whiteUsername` VARCHAR(255),
              `blackUsername` VARCHAR(255),
              `gameName` VARCHAR(255) NOT NULL,
               
              PRIMARY KEY (`id`)
            );
            """


                    //int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
    };

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Integer createGame(String authToken, String gameName) {
        return 0;
    }

    @Override
    public ArrayList<GameData> listOfGames() {
        return null;
    }

    @Override
    public void clearGame() {

    }

    @Override
    public void joinGame(ChessGame.TeamColor playerColor, Integer gameID, String username) {

    }
}
