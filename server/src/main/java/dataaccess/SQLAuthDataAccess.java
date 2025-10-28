package dataaccess;

public class SQLAuthDataAccess {
    public SQLAuthDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
    }


    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
               id INT NOT NULL AUTO_INCREMENT,
              `username` VARCHAR(255) NOT NULL,
              `authToken` VARCHAR(255) NOT NULL,
            );
            """
    };

}
