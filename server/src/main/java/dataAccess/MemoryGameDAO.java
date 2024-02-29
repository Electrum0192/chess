package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryGameDAO implements GameDAO{
    private static Collection<GameData> games;
    private static int nextID;

    private static final MemoryGameDAO instance = new MemoryGameDAO();

    private MemoryGameDAO() {
        games = new HashSet<GameData>();
        nextID = 0;
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
        GameData newGame = new GameData(nextID,null,null,gameName,new ChessGame());
        games.add(newGame);
        nextID++;
        return newGame.gameID();
    }

    @Override
    public GameData getGame(int gameID) {
        for(var i : games){
            if(i.gameID() == gameID){
                return i;
            }
        }
        return null;
    }

    /**
     * Rewrite the GameData object in memory with new information. Will rewrite the game with the same gameID.
     * If no game with that ID exists in memory, will do nothing.
     * @param game A GameData object with the new parameters.
     */
    public void setGame(GameData game){
        for(var i : games){
            if(i.gameID() == game.gameID()){
                games.remove(i);
                games.add(game);
                return;
            }
        }
    }

}
