package clientTests;

import dataAccess.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    private static String URL;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        StringBuilder serverUrl = new StringBuilder();
        serverUrl.append("http://localhost:");
        serverUrl.append(port);
        URL = serverUrl.toString();
    }

    @BeforeEach
    public void reset() throws Exception {
        ServerFacade.clear(URL);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("Register")
    public void register() throws Exception {
        //Positive case
        ServerFacade.register(URL, new UserData("Steve","incorrect","email"));
        Assertions.assertNotNull(new SQLUserDAO().getUser("Steve"),"User was not successfully created.");

        //Negative cases
        //Bad Request
        Exception exception = assertThrows(Exception.class, () -> {ServerFacade.register(URL,new UserData("Martin",null,null));});
        Assertions.assertTrue(exception.getMessage().contains("400"),"Server did not throw 400 exception.");
        //Already Taken
        Exception exception2 = assertThrows(Exception.class, () -> {ServerFacade.register(URL,new UserData("Steve","incorrect",null));});
        Assertions.assertTrue(exception2.getMessage().contains("403"),"Server did not throw 403 exception.");
    }

}
