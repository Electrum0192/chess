package serviceTests;

import chess.ChessGame;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.GameService;
import service.UserService;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceTests {

    private static GameService service;
    private static UserService userService;
    private static MemoryGameDAO memoryGameDAO;
    private static GameData newGame;
    private static UserData newUser;
    private static AuthData newAuth;

    @BeforeAll
    public static void init() throws Exception {
        service = new GameService();
        memoryGameDAO = MemoryGameDAO.getInstance();
        newGame = new GameData(1,"white","black","newgame",new ChessGame());
        newUser = new UserData("newusername","newpassword","newemail");
        userService = new UserService();
        newAuth = userService.register(newUser);
    }

    @BeforeEach
    public void setup(){
        memoryGameDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Clear")
    public void clear() throws Exception{
        service.createGame(newAuth.authToken(), "newGame");
        service.clearGames();
        Assertions.assertTrue(memoryGameDAO.listGames().isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("List Games")
    public void listGames() throws Exception{
        service.createGame(newAuth.authToken(), "newGame");
        service.createGame(newAuth.authToken(), "newGame1");
        service.createGame(newAuth.authToken(), "newGame2");
        Assertions.assertEquals(3, memoryGameDAO.listGames().size());
    }

    @Test
    @Order(3)
    @DisplayName("Create Game")
    public void createGame() throws Exception{
        service.createGame(newAuth.authToken(), "newGame");
        Collection<String> names = new HashSet<String>();
        for(var i : memoryGameDAO.listGames()){names.add(i.gameName());} //Get set of names
        Assertions.assertTrue(names.contains("newGame"), "Game successfully created");

        Exception exception = assertThrows(Exception.class, () -> service.createGame("nope","anotherNewGame"));
        Assertions.assertTrue(exception.getMessage().contains("Error: unauthorized"));

        Exception exception2 = assertThrows(Exception.class, () -> service.createGame(newAuth.authToken(),"newGame"));
        Assertions.assertTrue(exception2.getMessage().contains("Error: bad request"));
    }
}
