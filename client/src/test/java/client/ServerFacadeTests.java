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

}
