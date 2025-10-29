package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDataAccess  implements AuthDataAccess{
    public SQLAuthDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
    }


    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  AuthData (
              `username` VARCHAR(255) NOT NULL,
              `authToken` VARCHAR(255) NOT NULL,
              PRIMARY KEY (`authToken`)
            );
            """
    };

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

    @Override
    public void clearAuth() {

    }

    @Override
    public void createAuth(AuthData authData) {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
