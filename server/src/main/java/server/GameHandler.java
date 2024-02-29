package server;

import com.google.gson.Gson;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class GameHandler {
    public static Object listGames(Request req, Response res) {
        GameService gameService = new GameService();
        var auth = new Gson().fromJson(req.headers("authorization"), String.class);
        try{
            GameList gameList = new GameList(gameService.listGames(auth));
            return new Gson().toJson(gameList);
        }catch (Exception e){
            var message = new Gson().toJson(new ErrorMessage(e.getMessage()));
            if(e.getMessage().contains("Error: unauthorized")){
                res.status(401);
                res.body(message);
            }else{
                res.status(500);
                res.body(new Gson().toJson(new ErrorMessage("Error: description")));
            }
            return message;
        }
    }
}
