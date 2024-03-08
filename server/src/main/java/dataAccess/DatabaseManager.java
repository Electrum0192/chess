package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Creates database and tables after checking if they already exist
     */
    public static void initialize(){
        //Create Database if it doesn't exist
        try{
            createDatabase();
        }catch (DataAccessException e){
            System.out.println("ERROR: DATABASE CREATION FAILED");
        }
        //Create User Table if it doesn't exist
        var createUserTable = """
            CREATE TABLE  IF NOT EXISTS users (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
            )""";
        try(var createTableStatement = getConnection().prepareStatement(createUserTable)){
            createTableStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        //Create Game Table if it doesn't exist
        var createGameTable = """
            CREATE TABLE  IF NOT EXISTS games (
                gameID INT NOT NULL AUTO_INCREMENT,
                whiteUsername VARCHAR(255),
                blackUsername VARCHAR(255),
                gameName VARCHAR(255),
                chessGame VARCHAR(255) NOT NULL,
                PRIMARY KEY (gameID)
            )""";
        try(var createTableStatement = getConnection().prepareStatement(createGameTable)){
            createTableStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        //Create Auth Table if it doesn't exist
        var createAuthTable = """
            CREATE TABLE  IF NOT EXISTS auths (
                username VARCHAR(255) NOT NULL,
                authToken VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
            )""";
        try(var createTableStatement = getConnection().prepareStatement(createAuthTable)){
            createTableStatement.executeUpdate();
        } catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Encryption function for secure passwords
     * @param input the unencrypted password
     * @return the encrypted version of that password
     */
    static String encrypt(String input){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(input);
    }

    /**
     * Updates the memory database with persistent data from the SQL database. Used on server startup.
     */
    public static void updateMemory(){
        try(var conn = getConnection()){
            //Update Users
            var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM users");
            try(var rs = preparedStatement.executeQuery()){
                MemoryUserDAO access = MemoryUserDAO.getInstance();
                while(rs.next()){
                    if(access.getUser(rs.getString("username")) == null){
                        access.createUser(rs.getString("username"), rs.getString("password"), rs.getString("email")); //TODO Adapt memory to use encrypted passwords
                    }
                }
            }
            //Update Games
            preparedStatement = conn.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM games");
            try(var rs = preparedStatement.executeQuery()){
                MemoryGameDAO access = MemoryGameDAO.getInstance();
                while(rs.next()){
                    if(access.getGame(rs.getInt("gameID")) == null){
                        ChessGame game = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
                        access.addGame(new GameData(rs.getInt("gameID"),rs.getString("whiteUsername"),rs.getString("blackUsername"),rs.getString("gameName"),game));
                    }
                }
            }
            //Update Auths
            preparedStatement = conn.prepareStatement("SELECT username, authToken FROM auths");
            try(var rs = preparedStatement.executeQuery()){
                MemoryAuthDAO access = MemoryAuthDAO.getInstance();
                while(rs.next()){
                    if(access.getAuth(rs.getString("authToken")) == null){
                        access.addAuth(rs.getString("username"),rs.getString("authToken"));
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
