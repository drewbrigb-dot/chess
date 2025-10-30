package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLAuthDataAccess  implements AuthDataAccess{
    public SQLAuthDataAccess() throws DataAccessException {
        createDatabase();
    }


    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  AuthData (
              `username` VARCHAR(255) NOT NULL,
              `authToken` VARCHAR(255) NULL,
              PRIMARY KEY (`authToken`)
            );
            """
    };

    public void createDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatement) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex ) {
            throw new DataAccessException("Unable to read data");
        }
    }

    @Override
    public void clearAuth() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE AuthData")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO AuthData (username,authToken) VALUES (?,?)")) {
                preparedStatement.setString(1, authData.username());
                preparedStatement.setString(2, authData.authToken());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            throw new DataAccessException("No parameter can be null");
        }

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM AuthData WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {

                    String usrnme = result.getString("username");
                    String authTokenFinal = result.getString("authToken");
                    AuthData authData = new AuthData(usrnme, authTokenFinal);
                    return authData;
                    //do I need to check for password?
                }
            }
            return null;
        }catch (SQLException e) {
            throw new DataAccessException("What the freak happened");
        }
    }

    @Override
    public void deleteAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM AuthData WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /*@Override
    public boolean isEmpty() {
        return false;
    }*/
}
