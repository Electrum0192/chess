package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.ContainerProvider;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacade extends Endpoint{

    public Session session;
    private ChessGame game;

    public static AuthData register(String url, UserData user) throws Exception{
        URI uri = new URI(url + "/user");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        StringBuilder body = new StringBuilder();
        body.append("{\"username\":\"");
        body.append(user.username());
        body.append("\", \"password\":\"");
        body.append(user.password());
        body.append("\", \"email\":\"");
        body.append(user.email());
        body.append("\"}");

        writeRequestBody(body.toString(), http);
        http.connect();

        if(http.getResponseCode() == 200){
            return (AuthData) readResponseBody(http, AuthData.class);
        }else{
            //ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(http.getResponseCode()+"");
        }
    }
    public static AuthData login(String url, UserData user) throws Exception {
        URI uri = new URI(url + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("POST");

        StringBuilder body = new StringBuilder();
        body.append("{\"username\":\"");
        body.append(user.username());
        body.append("\", \"password\":\"");
        body.append(user.password());
        body.append("\"}");

        writeRequestBody(body.toString(), http);
        http.connect();

        if(http.getResponseCode() == 200){
            return (AuthData) readResponseBody(http, AuthData.class);
        }else{
            //ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(http.getResponseCode()+"");
        }
    }
    public static boolean logout(String url, String authToken) throws Exception{
        URI uri = new URI(url + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setRequestMethod("DELETE");

        http.setDoOutput(true);
        http.addRequestProperty("authorization", authToken);
        http.connect();

        if(http.getResponseCode() == 200){
            return true;
        }else{
            //ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(http.getResponseCode()+"");
        }

    }
    public static int create(String url, String authToken, String gamename) throws Exception{
        URI uri = new URI(url + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setRequestMethod("POST");

        http.setDoOutput(true);
        http.addRequestProperty("authorization", authToken);

        StringBuilder body = new StringBuilder();
        body.append("{\"gameName\":\"");
        body.append(gamename);
        body.append("\"}");

        writeRequestBody(body.toString(), http);
        http.connect();
        if(http.getResponseCode() == 200){
            GameID ID = (GameID) readResponseBody(http, GameID.class);
            return ID.gameID();
        }else{
            //ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(http.getResponseCode()+"");
        }
    }
    public static GameList list(String url, String authToken) throws Exception{
        URI uri = new URI(url + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setRequestMethod("GET");

        http.setDoOutput(true);
        http.addRequestProperty("authorization", authToken);

        http.connect();
        if(http.getResponseCode() == 200){
            return (GameList) readResponseBody(http, GameList.class);
        }else{
            //ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(http.getResponseCode()+"");
        }
    }
    public static UserGameCommand join(String url, String authToken, int gameID, String requestColor) throws Exception{
        URI uri = new URI(url + "/game");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setRequestMethod("PUT");

        http.setDoOutput(true);
        http.addRequestProperty("authorization", authToken);

        StringBuilder body = new StringBuilder();
        body.append("{\"playerColor\":\"");
        body.append(requestColor.toUpperCase());
        body.append("\", \"gameID\":");
        body.append(gameID);
        body.append("}");
        writeRequestBody(body.toString(), http);

        http.connect();

        if(http.getResponseCode() != 200){
            //ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(http.getResponseCode()+"");
        }

        ChessGame.TeamColor team;
        if(requestColor.toUpperCase().equals("WHITE")){
            team = ChessGame.TeamColor.WHITE;
            return new JoinPlayer(authToken, gameID, team);
        }else if(requestColor.toUpperCase().equals("BLACK")){
            team = ChessGame.TeamColor.BLACK;
            return new JoinPlayer(authToken, gameID, team);
        }else {
            return new JoinObserver(authToken, gameID);
        }
    }
    public static void clear(String url) throws Exception{
        URI uri = new URI(url + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setRequestMethod("DELETE");
        http.connect();

        if(http.getResponseCode() != 200){
            //ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(http.getResponseCode()+"");
        }
    }

    public void connect(String url) throws Exception{
        //Convert URL from http to ws
        url = url.replaceFirst("http","ws");
        //Connect
        URI uri = new URI(url + "/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                //Decode message to ServerMessage
                try{
                    var response = new Gson().fromJson(message, ServerMessage.class);
                    if(response.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)){
                        LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                        game = loadGame.load();
                        if(Client.getTeam().equals("BLACK")){
                            BoardPrinter.printBlack(game.getBoard());
                        }else{
                            BoardPrinter.printWhite(game.getBoard());
                        }
                    }else if(response.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)){
                        Error error = new Gson().fromJson(message, Error.class);
                        System.out.println("<ERROR>: "+error.getErrorMessage());
                    }else if(response.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)){
                        Notification notification = new Gson().fromJson(message, Notification.class);
                        System.out.println("<SERVER>: "+notification.getMessage());
                    }
                }catch (Exception e){
                    System.out.println(message);
                }
            }
        });
    }

    public void send(String msg) throws Exception{
        this.session.getBasicRemote().sendText(msg);
    }

    private static void writeRequestBody(String body, HttpURLConnection http) throws IOException {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static <T> T readResponseBody(HttpURLConnection http, Class<T> c) throws IOException {
        T responseBody;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = (T) new Gson().fromJson(inputStreamReader, c);
            return responseBody;
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public static void readError(int errorCode){
        String display = switch (errorCode) {
            case 400 -> "Error: Server returned 400 (Bad Request). Double check your input parameters.";
            case 401 ->
                    "Error: Server returned 401 (Unauthorized). Double check your credentials or try a different command.";
            case 403 ->
                    "Error: Server returned 403 (Already Taken). Make sure you aren't trying to create something that already exists.";
            case 500 ->
                    "Error: Server returned 500 (Unknown Error). Congratulations on breaking something that wasn't meant to be broken.";
            default -> "";
        };
        System.out.println(display);
    }

    public ChessGame getGame() {
        return game;
    }
}
