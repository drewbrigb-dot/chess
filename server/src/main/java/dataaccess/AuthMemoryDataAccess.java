
package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.function.Executable;

import java.util.HashMap;

public class AuthMemoryDataAccess implements AuthDataAccess {
    private final HashMap<String, AuthData> usersAuth = new HashMap<>();

    @Override
    public void clearAuth() {
        usersAuth.clear();
    }

    @Override
    public void createAuth(AuthData authData) {
        usersAuth.put(authData.authToken(),authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return usersAuth.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        usersAuth.remove(authToken);
    }

   /* @Override
    public boolean isEmpty() {
        return usersAuth.isEmpty();
    }*/

    @Override
    public void createDatabase() throws DataAccessException {
        System.console().printf("You're not supposed to be here");
    }
}

