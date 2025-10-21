package dataaccess;


import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Objects;

public class UserMemoryDataAccess implements UserDataAccess {

    private final HashMap<String,UserData> users = new HashMap<>();



    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void createUser(UserData user) {
        users.put(user.username(),user);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }
    public boolean isEmpty () {
        return users.isEmpty();
    }

    public boolean validateUser(String username, String password) {

        UserData user = getUser(username);
        if (users.get(username) != null && Objects.equals(user.password(), password)) {
            return true;
        }else {return false;}

    }
}
