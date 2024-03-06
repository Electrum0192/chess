package dataAccess;

import com.google.gson.Gson;
import model.Game;
import model.GameData;
import chess.ChessGame;

import java.sql.Statement;
import java.util.Collection;
import java.util.HashSet;

public class SQLGameDAO implements GameDAO{
    @Override
    public void clear() {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games");
            preparedStatement.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Collection<Game> listGames() {
        Collection<Game> list = new HashSet<>();

        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName FROM games");
            try(var rs = preparedStatement.executeQuery()){
                while(rs.next()){
                    var gameID = rs.getInt("gameID");
                    var whiteUsername = rs.getString("whiteUsername");
                    var blackUsername = rs.getString("blackUsername");
                    var gameName = rs.getString("gameName");

                    Game game = new Game(gameID,whiteUsername,blackUsername,gameName);
                    list.add(game);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return list;
    }

    @Override
    public int createGame(String gameName) {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("INSERT INTO games (whiteUsername, blackUsername, gameName) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, null);
            preparedStatement.setString(2, null);
            preparedStatement.setString(3, gameName);

            preparedStatement.executeUpdate();

            var rs = preparedStatement.getGeneratedKeys();
            var ID = 0;
            if(rs.next()){
                ID = rs.getInt(1);
            }
            return ID;

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return -1;
    }

    @Override
    public GameData getGame(int gameID) {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("SELECT whiteUsername, blackUsername, gameName, chessGame FROM games WHERE gameID=?");
            preparedStatement.setInt(1, gameID);
            try(var rs = preparedStatement.executeQuery()){
                var whiteUsername = rs.getString("whiteUsername");
                var blackUsername = rs.getString("blackUsername");
                var gameName = rs.getString("gameName");
                var gameString = rs.getString("chessGame");

                ChessGame game = new Gson().fromJson(gameString, ChessGame.class);

                return new GameData(gameID,whiteUsername,blackUsername,gameName,game);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void updateGame(int gameID, String gameString) {
        try(var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement("UPDATE games SET chessGame=? WHERE gameID=?");
            preparedStatement.setString(1,gameString);
            preparedStatement.setInt(2,gameID);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
