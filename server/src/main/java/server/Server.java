package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearApp);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);

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
    private Object register(Request req, Response res){
        return UserHandler.register(req,res);
    }
    private Object login(Request req, Response res){
        return UserHandler.login(req,res);
    }
}
