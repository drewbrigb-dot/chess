package dataaccess;

import com.google.gson.Gson;

import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import model.*;

public class SQLUserDataAccess implements UserDataAccess{
    public SQLUserDataAccess() throws Exception {
        createDatabase();

    }
    @Override
    public void clear() {

    }

    @Override
    public void createUser(UserData user) {
        var statement = "INSERT INTO UserData (username,email,password) VALUES (?,?,?)";
        String json = new Gson().toJson(user);

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean validateUser(String username, String password) {
        return false;
    }


    //make create tables function
    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
               id INT NOT NULL AUTO_INCREMENT,
              `username` VARCHAR(255) NOT NULL,
              `email` VARCHAR(255) NOT NULL,
              `password` VARCHAR(255) NOT NULL
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
            throw new Exception("Unable to read data");

        }
    }
}
