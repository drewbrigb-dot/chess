package dataaccess;

import com.google.gson.Gson;

import model.UserData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.*;
import service.UserService;

public class SQLUserDataAccess implements UserDataAccess{
    public SQLUserDataAccess() throws Exception {
        createDatabase();

    }
    @Override
    public void clear() throws Exception{
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE UserData")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUser(UserData user) throws Exception{
        /*var statement = "INSERT INTO UserData (username,email,password) VALUES (?,?,?)";
        String json = new Gson().toJson(user);*/

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO UserData (username,email,password) VALUES (?,?,?)")) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.email());
                preparedStatement.setString(3, user.password());
                preparedStatement.executeUpdate();
            }
        }catch (Exception e) {
            throw new Exception("No parameter can be null");
        }
    }

    @Override
    public UserData getUser(String username) throws Exception {

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM UserData WHERE username=?")) {
                preparedStatement.setString(1, username);
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    String usrnme = result.getString("username");
                    String email = result.getString("email");
                    String password = result.getString("password");
                    return new UserData(usrnme,email,password);
                }



            }
    }catch (Exception e) {
            throw new Exception("User Data not found");
        }

        return null;
    }

    /*@Override
    public boolean isEmpty() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM UserData")) {
                var result = preparedStatement.executeQuery();
                if (result.next()) {
                    String usrnme = result.getString("username");

                    return new UserData(usrnme,email,password);
                }


            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }*/

    @Override
    public boolean validateUser(String username, String password) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM UserData WHERE username=?")) {
                preparedStatement.setString(1, username);
                ResultSet result = preparedStatement.executeQuery();
                if (result.next()) {
                    return true;
                   /* String usrnme = result.getString("username");
                    String email = result.getString("email");
                    String psswrd = result.getString("password");*/

                    //do I need to check for password?
                }else {return false;}
            }
        }catch (Exception e) {
            throw new Exception("What the freak happened");
        }
    }


    //make create tables function
    private final String[] createStatement = {
            """
            CREATE TABLE IF NOT EXISTS  UserData (
              `username` VARCHAR(255) NOT NULL,
              `email` VARCHAR(255) NOT NULL,
              `password` VARCHAR(255) NOT NULL,
               PRIMARY KEY (`username`)
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
}
