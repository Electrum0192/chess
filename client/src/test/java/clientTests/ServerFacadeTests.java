package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import service.GameService;
import service.UserService;


public class ServerFacadeTests {

    private static Server server;
    private static UserService userService;
    private static GameService gameService;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public static void reset(){
        //CLEAR DATABASE
        userService.clearUsers();
        gameService.clearGames();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
