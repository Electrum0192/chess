package clientTests;

import dataAccess.SQLUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.GameService;
import ui.ServerFacade;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        exception = assertThrows(Exception.class, () -> {ServerFacade.register(URL,new UserData("Steve","incorrect",null));});
        Assertions.assertTrue(exception.getMessage().contains("403"),"Server did not throw 403 exception.");
    }

    @Test
    @DisplayName("Login")
    public void login() throws Exception {
        //Positive case
        ServerFacade.register(URL, new UserData("Steve","incorrect","email"));
        AuthData auth = ServerFacade.login(URL, new UserData("Steve", "incorrect", null));
        Assertions.assertNotNull(auth,"User was not successfully logged in.");

        //Negative cases
        //Unauthorized (User does not exist)
        Exception exception = assertThrows(Exception.class, () -> {ServerFacade.login(URL,new UserData("Martin","incorrect",null));});
        Assertions.assertTrue(exception.getMessage().contains("401"),"Server did not throw 401 exception.");
        //Unauthorized (Wrong password)
        exception = assertThrows(Exception.class, () -> {ServerFacade.login(URL,new UserData("Steve","actuallyIncorrect",null));});
        Assertions.assertTrue(exception.getMessage().contains("401"),"Server did not throw 401 exception.");
    }

    @Test
    @DisplayName("Logout")
    public void logout() throws Exception {
        //Positive case
        ServerFacade.register(URL, new UserData("Steve","incorrect","email"));
        AuthData auth = ServerFacade.login(URL, new UserData("Steve", "incorrect", null));
        ServerFacade.logout(URL,auth.authToken());
        Assertions.assertEquals(2, getDatabaseRows(), "User was not successfully logged out.");

        //Negative cases
        //Unauthorized
        Exception exception = assertThrows(Exception.class, () -> {ServerFacade.logout(URL,"BADAUTHNOPEDONTDOIT");});
        Assertions.assertTrue(exception.getMessage().contains("401"),"Server did not throw 401 exception.");

    }

    @Test
    @DisplayName("Create")
    public void create() throws Exception {
        //Positive case
        AuthData auth = ServerFacade.register(URL, new UserData("Steve","incorrect","email"));
        ServerFacade.create(URL,auth.authToken(),"newgame");
        Assertions.assertEquals(3, getDatabaseRows(), "Game was not successfully created");

        //Negative cases
        //Unauthorized
        Exception exception = assertThrows(Exception.class, () -> {ServerFacade.create(URL,"BADAUTHNOPEDONTDOIT","game2");});
        Assertions.assertTrue(exception.getMessage().contains("401"),"Server did not throw 401 exception.");
        //Bad Request
        exception = assertThrows(Exception.class, () -> {ServerFacade.create(URL, auth.authToken(),"newgame");});
        Assertions.assertTrue(exception.getMessage().contains("400"),"Server did not throw 400 exception.");

    }

    private int getDatabaseRows() {
        int rows = 0;
        try {
            Class<?> clazz = Class.forName("dataAccess.DatabaseManager");
            Method getConnectionMethod = clazz.getDeclaredMethod("getConnection");
            getConnectionMethod.setAccessible(true);

            Object obj = clazz.getDeclaredConstructor().newInstance();
            try (Connection conn = (Connection) getConnectionMethod.invoke(obj);) {
                try (var statement = conn.createStatement()) {
                    for (String table : getTables(conn)) {
                        var sql = "SELECT count(*) FROM " + table;
                        try (var resultSet = statement.executeQuery(sql)) {
                            if (resultSet.next()) {
                                rows += resultSet.getInt(1);
                            }
                        }
                    }
                }

            }
        } catch (Exception ex) {
            Assertions.fail("Unable to load database in order to verify persistence. Are you using dataAccess.DatabaseManager to set your credentials?");
        }

        return rows;
    }

    private List<String> getTables(Connection conn) throws SQLException {
        String sql = """
                    SELECT table_name
                    FROM information_schema.tables
                    WHERE table_schema = DATABASE();
                """;

        List<String> tableNames = new ArrayList<>();
        try (var preparedStatement = conn.prepareStatement(sql)) {
            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tableNames.add(resultSet.getString(1));
                }
            }
        }

        return tableNames;
    }

}
