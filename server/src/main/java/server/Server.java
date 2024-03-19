package server;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        DatabaseManager.initialize();
        //DatabaseManager.updateMemory();

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
}
