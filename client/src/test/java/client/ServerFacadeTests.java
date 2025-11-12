import chess.ChessGame;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import Server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + port;
        facade = new ServerFacade(url);
    }
    @BeforeEach
    void clearDatabase() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void register() throws Exception {
        UserData userData = new UserData("Ozzy Ozbourne", "ozzy@gmail.com", "offtherails");
        var authData = facade.register(userData);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void registerBad() throws Exception {
        UserData userData = new UserData("Ozzy Ozbourne", null, "offtherails");
        Exception e = assertThrows(Exception.class, () -> facade.register(userData)) ;
        assertEquals("Error: No parameter can be null",e.getMessage());
    }

    @Test
    public void login() throws Exception {
        UserData userData = new UserData("Ozzy Ozbourne", "ozzy@gmail.com", "offtherails");
        facade.register(userData);
        var authData = facade.login(userData);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void loginBad() throws Exception {
        UserData userData = new UserData("Ozzy Ozbourne", "ozzy@gmail.com", "offtherails");
        Exception e = assertThrows(Exception.class, () -> facade.login(userData)) ;
        assertEquals("Error: unauthorized",e.getMessage());
    }

    @Test
    public void logout() throws Exception {
        UserData userData = new UserData("Ozzy Ozbourne", "ozzy@gmail.com", "offtherails");
        AuthData authData = facade.register(userData);
        assertDoesNotThrow(() -> facade.logout(authData.authToken()));
    }

    @Test
    public void logoutBad() throws Exception {
        UserData userData = new UserData("Ozzy Ozbourne", "ozzy@gmail.com", "offtherails");
        facade.register(userData);
        Exception e = assertThrows(Exception.class, () -> facade.logout("poop&pee")) ;
        assertEquals("Error: unauthorized",e.getMessage());
    }

    @Test
    public void createGame() throws Exception {
        UserData userData = new UserData("Ozzy Ozbourne", "ozzy@gmail.com", "offtherails");
        AuthData authData = facade.register(userData);
        assertDoesNotThrow(() -> facade.createGame(authData.authToken(),"ChessIsStupid"));
    }

    @Test
    public void createBadGame() throws Exception {
        UserData userData = new UserData("Ozzy Ozbourne", "ozzy@gmail.com", "offtherails");
        AuthData authData = facade.register(userData);
        Exception e = assertThrows(Exception.class, () -> facade.createGame(authData.authToken(),null)) ;
        assertEquals("Error: bad request",e.getMessage());
    }

    @Test
    public void joinGame() throws Exception {
        UserData userData = new UserData("Ozzy Ozbourne", "ozzy@gmail.com", "offtherails");
        AuthData authData = facade.register(userData);
        Integer gameID = facade.createGame(authData.authToken(),"ChessIsStupid");
        assertDoesNotThrow(() -> facade.joinGame(ChessGame.TeamColor.WHITE,gameID,authData.authToken()));
    }

    @Test
    public void joinBadGame() throws Exception {
        UserData userData = new UserData("Ozzy Ozbourne", "ozzy@gmail.com", "offtherails");
        AuthData authData = facade.register(userData);
        Integer gameID = facade.createGame(authData.authToken(),"ChessIsStupid");
        Integer badGameID = 45;
        Exception e = assertThrows(Exception.class, () -> facade.joinGame(ChessGame.TeamColor.WHITE,badGameID,authData.authToken())) ;
        assertEquals("Error: unauthorized",e.getMessage());
    }











}
