package dataAccessTests;


import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.UserHandler;
import service.GameService;
import service.UserService;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
public class DataTests {

    static SQLGameDAO gameDAO;
    static SQLUserDAO userDAO;
    static SQLAuthDAO authDAO;
    static UserService userService;
    static GameService gameService;

    static UserData newUser;

    @BeforeAll
    public static void init(){
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        userService = new UserService();
        gameService = new GameService();

        newUser = new UserData("newname", "newpass", "newemail");
    }

    @BeforeEach
    public void setup(){
        userService.clearUsers();
        gameService.clearGames();
    }

    @Test
    @Order(1)
    @DisplayName("Clear")
    public void clear() throws Exception{
        //Create some users and games
        userDAO.createUser("newname","newpass","newemail");
        gameDAO.createGame("newgame");

        //Clear
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();

        //Check database
        Assertions.assertEquals(0, getDatabaseRows(), "Database contains data after clear");
    }

    @Test
    @Order(1)
    @DisplayName("Create User")
    public void createUser() throws Exception{
        //Create a user
        userService.register(newUser);

        //Check database
        Assertions.assertEquals(2, getDatabaseRows(), "Database has incorrect number of rows"); //One row for the user, one for the auth
        Assertions.assertNotNull(userDAO.getUser("newname"), "User missing from database");

        //Negative case
        Exception exception = assertThrows(Exception.class, () -> {userService.register(newUser);});
        Assertions.assertTrue(exception.getMessage().contains("Error: already taken"));
    }

    @Test
    @Order(3)
    @DisplayName("Get User")
    public void getUser() throws Exception{
        userService.register(newUser);

        //Check database
        Assertions.assertTrue(userDAO.getUser("newname").email().equals("newemail"), "User not found");

        //Negative case
        Assertions.assertNull(userDAO.getUser("badname"), "getUser incorrectly returned a non-null");

    }

    @Test
    @Order(1)
    @DisplayName("Create Game")
    public void createGame() throws Exception{
        //Create a game
        AuthData auth = userService.register(newUser);
        int ID = gameService.createGame(auth.authToken(),"newgame");

        //Check database
        Assertions.assertEquals(3, getDatabaseRows(), "Database has incorrect number of rows");
        Assertions.assertNotNull(gameDAO.getGame(ID), "User missing from database");

        //Negative case
        Exception exception = assertThrows(Exception.class, () -> {gameService.createGame(auth.authToken(),"newgame");});
        Assertions.assertTrue(exception.getMessage().contains("Error: bad request"));
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
