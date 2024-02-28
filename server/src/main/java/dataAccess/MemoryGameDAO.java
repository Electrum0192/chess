package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
    private static Collection<GameData> games;

    private static final MemoryGameDAO instance = new MemoryGameDAO();

    private MemoryGameDAO() {
        games = new HashSet<GameData>();
    }
    public static MemoryGameDAO getInstance(){return instance;}

    @Override
    public void clear() {
        games.clear();
    }
    @Override
    public Collection<GameData> listGames(){return games;}

    @Override
    public int createGame(String gameName) {
        GameData newGame = new GameData(games.size(),null,null,gameName,new ChessGame());
        games.add(newGame);
        return newGame.gameID();
    }
}
