package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler {
    public static String clearApp(){
        UserService userService = new UserService();
        userService.clearUsers();
        GameService gameService = new GameService();
        gameService.clearGames();
        return new Gson().toJson(null);
    }
    public static String register(Request req, Response res){
        UserService userService = new UserService();
        var user = new Gson().fromJson(req.body(), UserData.class);
        try {
            return new Gson().toJson(userService.register(user));
        }
        catch (Exception e){
            var message = new Gson().toJson(new ErrorMessage(e.getMessage()));
            if(e.getMessage().contains("Error: already taken")){
                res.status(403);
                res.body(message);
            }
            return message;
        }
    }
}
