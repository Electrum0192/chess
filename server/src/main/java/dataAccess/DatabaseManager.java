package dataAccess;

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
                gameID INT NOT NULL,
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
                authtoken VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
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
}
