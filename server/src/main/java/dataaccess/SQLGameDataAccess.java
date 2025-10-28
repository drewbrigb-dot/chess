package dataaccess;

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
              `` VARCHAR(255) NOT NULL,
              `` VARCHAR(255) NOT NULL,
              `` VARCHAR(255) NOT NULL
            );
            """
    };
}
