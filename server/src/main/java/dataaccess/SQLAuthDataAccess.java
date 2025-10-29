package dataaccess;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDataAccess {
    public SQLAuthDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
    }


    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  AuthData (
               id INT NOT NULL AUTO_INCREMENT,
              `username` VARCHAR(255) NOT NULL,
              `authToken` VARCHAR(255) NOT NULL,
              PRIMARY KEY (`id`)
            );
            """
    };

    private void createDatabase() throws Exception {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatement) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                    //what is execute update
                }
            }
        } catch (SQLException ex ) {
            //what is this line
        }
    }

}
