package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    /**
     * Delete all game data from database. Used in testing.
     */
    public default void clear(){}

    /**
     * Create a new game
     * @return gameID
     */
    public default int createGame(){
        return 0;
    }

    /**
     * Retrieve a specified game and its info
     * @param gameID
     * @return GameData record for the game with that ID
     */
    public default GameData getGame(int gameID){
        return null;
    }

    /**
     * Retrieve info for all games
     * @return Collection of GameData records
     */
    public default Collection<GameData> listGames(){
        return null;
    }

    /**
     * Update a chess game, replacing the game string. Used when joining a game or making a move.
     * @param gameID
     * @param gameString The new game string to override the current string
     */
    public default void updateGame(int gameID, String gameString){

    }
}
