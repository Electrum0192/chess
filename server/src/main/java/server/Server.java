package server;

import dataAccess.DatabaseManager;
import org.eclipse.jetty.websocket.api.Session;
import spark.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Server {

    private static Map<Integer, HashSet<Session>> users;

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        DatabaseManager.initialize();
        //DatabaseManager.updateMemory();

        //Websocket Endpoint
        Spark.webSocket("/connect", WSHandler.class);
        users = new HashMap<>();

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearApp);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game",this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clearApp(Request req, Response res){
        return UserHandler.clearApp(res);
    }
    private Object register(Request req, Response res){return UserHandler.register(req,res);}
    private Object login(Request req, Response res){
        return UserHandler.login(req,res);
    }
    private Object logout(Request req, Response res){
        return UserHandler.logout(req,res);
    }
    private Object listGames(Request req, Response res){
        return GameHandler.listGames(req,res);
    }
    private Object createGame(Request req, Response res){
        return GameHandler.createGame(req,res);
    }
    private Object joinGame(Request req, Response res){return GameHandler.joinGame(req,res);}

    public static void addPlayer(int gameID, Session user){
        if(users.containsKey(gameID)){
            users.get(gameID).add(user);
        }else {
            HashSet<Session> sessions = new HashSet<>();
            sessions.add(user);
            users.put(gameID, sessions);
        }
    }
    public static void removePlayer(int gameID, Session user){
        users.get(gameID).remove(user);
        if(users.get(gameID).isEmpty()){
            users.remove(gameID);
        }
    }
    public static HashSet<Session> getSessions(int gameID){
        return users.get(gameID);
    }

    public static void clearPlayers(){
        users.clear();
    }

}
