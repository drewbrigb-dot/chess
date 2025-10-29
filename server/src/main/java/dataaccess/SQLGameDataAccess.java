package dataaccess;

import chess.ChessGame;
import model.UserData;

public class SQLGameDataAccess {
    public SQLGameDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
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
}
