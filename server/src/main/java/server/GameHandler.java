package server;

import com.google.gson.Gson;
import model.GameData;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class GameHandler {
    public static Object listGames(Request req, Response res) {
        GameService gameService = new GameService();
        try{
            var auth = parseAuth(req,res);
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

    public static Object createGame(Request req, Response res) {
        GameService gameService = new GameService();
        var gameName = new Gson().fromJson(req.body(), GameData.class);
        try{
            var auth = parseAuth(req,res);
            GameID ID = new GameID(gameService.createGame(auth,gameName.gameName()));
            return new Gson().toJson(ID);
        }catch (Exception e){
            var message = new Gson().toJson(new ErrorMessage(e.getMessage()));
            if(e.getMessage().contains("Error: unauthorized")){
                res.status(401);
                res.body(message);
            }else if(e.getMessage().contains("Error: bad request")){
                res.status(400);
                res.body(message);
            }else{
                res.status(500);
                res.body(new Gson().toJson(new ErrorMessage("Error: description")));
            }
            return message;
        }
    }

    public static Object joinGame(Request req, Response res){
        GameService gameService = new GameService();
        var request = new Gson().fromJson(req.body(), JoinRequest.class);
        try{
            var auth = parseAuth(req,res);
            gameService.joinGame(auth, request.gameID(), request.playerColor());
            return new Gson().toJson(null);
        }catch (Exception e){
            var message = new Gson().toJson(new ErrorMessage(e.getMessage()));
            if(e.getMessage().contains("Error: unauthorized")){
                res.status(401);
                res.body(message);
            }else if(e.getMessage().contains("Error: bad request")){
                res.status(400);
                res.body(message);
            }else if(e.getMessage().contains("Error: already taken")){
                res.status(403);
                res.body(message);
            }else{
                res.status(500);
                res.body(new Gson().toJson(new ErrorMessage("Error: description")));
            }
            return message;
        }
    }

    private static String parseAuth(Request req, Response res) throws Exception{
        try{
            return new Gson().fromJson(req.headers("authorization"), String.class);
        }catch(Exception e){
            res.status(401);
            res.body("Error: unauthorized");
            throw new Exception("Error: unauthorized");
        }
    }
}
