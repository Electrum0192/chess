package service;

import chess.ChessGame;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;
import model.Game;

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
    public Collection<Game> listGames(String authToken) throws Exception {
        //Have valid authToken
        MemoryAuthDAO authAccess = MemoryAuthDAO.getInstance();
        if(authAccess.getAuth(authToken) == null){
            throw new Exception("Error: unauthorized");
        }
        //Get Games
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
        if(authAccess.getAuth(authToken) == null){
            throw new Exception("Error: unauthorized");
        }
        //Does game with that name already exist?
        MemoryGameDAO access = MemoryGameDAO.getInstance();
        Collection<Game> games = access.listGames();
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
    public void joinGame(String authToken, int gameID, ChessGame.TeamColor requestColor) throws Exception {
        //Is User authorized?
        MemoryAuthDAO authAccess = MemoryAuthDAO.getInstance();
        if(authAccess.getAuth(authToken) == null) {
            throw new Exception("Error: unauthorized");
        }
        //Does game with that ID exist?
        MemoryGameDAO access = MemoryGameDAO.getInstance();
        if(access.getGame(gameID) == null){
            throw new Exception("Error: bad request");
        }
        //Is requested color open
        if(requestColor == ChessGame.TeamColor.WHITE){
            if(access.getGame(gameID).whiteUsername() != null){
                throw new Exception("Error: already taken");
            }
        }else if(requestColor == ChessGame.TeamColor.BLACK){
            if(access.getGame(gameID).blackUsername() != null){
                throw new Exception("Error: already taken");
            }
        }
        //Join Game
        AuthData auth = authAccess.getAuth(authToken);
        String username = auth.username();
        GameData game = access.getGame(gameID);
        GameData newGame;
        if(requestColor == ChessGame.TeamColor.WHITE){
            newGame = new GameData(gameID,username,game.blackUsername(),game.gameName(),game.game());
        }else if(requestColor == ChessGame.TeamColor.BLACK){
            newGame = new GameData(gameID,game.whiteUsername(),username,game.gameName(),game.game());
        }else{
            //Observer
            newGame = game;
        }
        access.setGame(newGame);
    }
}
