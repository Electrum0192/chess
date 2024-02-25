package service;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public class GameService {
    /**
     * Delete all game data from database. Used in testing.
     */
    public void clearGames(){}

    /**
     * Retrieve info for all games
     * @return Collection of GameData records
     */
    public Collection<GameData> listGames(){
        return null;
    }
    /**
     * Create a new game
     * @return gameID
     */
    public int createGame(){
        return 0;
    }

    /**
     * Join an existing game
     * @param gameID ID of the game you'd like to join
     * @param requestColor Team color you'd like to join as
     */
    public void joinGame(int gameID, ChessGame.TeamColor requestColor){}
}
