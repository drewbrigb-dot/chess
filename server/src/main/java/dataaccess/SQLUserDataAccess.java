package dataaccess;

import model.UserData;

public class SQLUserDataAccess implements UserDataAccess{
    public SQLUserDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
    @Override
    public void clear() {

    }

    @Override
    public void createUser(UserData user) {

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
            );
            """
    };
}
