package ui;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class ServerFacade {

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
            ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(response.message());
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
            ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(response.message());
        }
    }
    public static boolean logout(String url, String authToken) throws Exception{
        URI uri = new URI(url + "/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

        http.setRequestMethod("DELETE");

        http.setDoOutput(true);
        http.addRequestProperty("authorization", authToken);
        http.connect();

        return http.getResponseCode() == 200;
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
            ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(response.message());
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
            ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(response.message());
        }
    }
    public static void join(String url, String authToken, int gameID, String requestColor) throws Exception{
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
            ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
            throw new Exception(response.message());
        }
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
}
