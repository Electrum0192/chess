package service;

import chess.ChessGame;
import dataAccess.GameDAO;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.UserDAO;
import model.GameData;

import java.util.Collection;

public class GameService {
    /**
     * Delete all game data from database. Used in testing.
     */
    public void clearGames(){
        MemoryGameDAO access = MemoryGameDAO.getInstance();
        access.clear();
    }

    /**
     * Retrieve info for all games
     * @return Collection of GameData records
     */
    public Collection<GameData> listGames(){
        MemoryGameDAO access = MemoryGameDAO.getInstance();
        return access.listGames();
    }
    /**
     * Create a new game
     * @return gameID
     */
    public int createGame(String authToken, String gameName) throws Exception {
        //Have valid authToken
        MemoryAuthDAO authAccess = MemoryAuthDAO.getInstance();
        if(authAccess.getAuth(authToken) == null || !authToken.equals(authAccess.getAuth(authToken).authToken())){
            throw new Exception("Error: unauthorized");
        }
        //Does game with that name already exist?
        MemoryGameDAO access = MemoryGameDAO.getInstance();
        Collection<GameData> games = access.listGames();
        for(var i : games){
            if(i.gameName().equals(gameName)){
                throw new Exception("Error: bad request");
            }
        }
        //Create new game
        return access.createGame(gameName);
    }

    /**
     * Join an existing game
     * @param gameID ID of the game you'd like to join
     * @param requestColor Team color you'd like to join as
     */
    public void joinGame(int gameID, ChessGame.TeamColor requestColor){}
}
