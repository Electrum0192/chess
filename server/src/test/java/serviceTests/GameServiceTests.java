package serviceTests;

import chess.ChessGame;
import dataAccess.MemoryGameDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.GameService;

public class GameServiceTests {

    private static GameService service;
    private static MemoryGameDAO memoryGameDAO;
    private static GameData newGame;

    @BeforeAll
    public static void init(){
        service = new GameService();
        memoryGameDAO = MemoryGameDAO.getInstance();
        newGame = new GameData(1,"white","black","newgame",new ChessGame());
    }

    @Test
    @Order(1)
    @DisplayName("Clear")
    public void clear() throws Exception{
        service.createGame();
        service.clearGames();
        Assertions.assertTrue(memoryGameDAO.getGameCollection().isEmpty());
    }
}
