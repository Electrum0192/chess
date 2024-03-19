import com.google.gson.Gson;
import model.AuthData;
import model.ErrorMessage;
import ui.EscapeSequences;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if(args.length == 1){
            serverUrl = args[0];
        }

        System.out.println(EscapeSequences.WHITE_QUEEN+" Welcome to 240 chess. Type \"Help\" to get started. "+EscapeSequences.WHITE_QUEEN);

        try {
            runClient(serverUrl);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void runClient(String serverUrl) throws Exception{
        AuthData authData;

        System.out.println(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE);

        //Loop for user input/client output
        Boolean run = true;
        String setting = "LOGGED_OUT";
        while(run){
            //Get user input
            System.out.printf("[%s] >>> ",setting);
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var command = line.split(" ");
            String action = command[0];

            //Read command and create the http request
            switch (setting) {
                case "LOGGED_OUT":
                    if (readEqual(action, "Help")) {
                        System.out.println(helpText(setting));
                    } else if (readEqual(action, "Quit")) {
                        run = false;
                    } else if (readEqual(action, "Login")) {
                        URI uri = new URI(serverUrl + "/session");
                        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

                        http.setRequestMethod("POST");

                        StringBuilder body = new StringBuilder();
                        body.append("{\"username\":\"");
                        body.append(command[1]);
                        body.append("\", \"password\":\"");
                        body.append(command[2]);
                        body.append("\"}");

                        writeRequestBody(body.toString(), http);
                        http.connect();

                        if (http.getResponseCode() == 200) {
                            authData = (AuthData) readResponseBody(http, AuthData.class);
                            System.out.printf("Logged in as: %s", authData.username());
                            setting = "LOGGED_IN";
                        } else {
                            ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
                        }
                    } else if (readEqual(action, "Register")) {
                        URI uri = new URI(serverUrl + "/user");
                        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

                        http.setRequestMethod("POST");

                        StringBuilder body = new StringBuilder();
                        body.append("{\"username\":\"");
                        body.append(command[1]);
                        body.append("\", \"password\":\"");
                        body.append(command[2]);
                        body.append("\", \"email\":\"");
                        body.append(command[3]);
                        body.append("\"}");

                        writeRequestBody(body.toString(), http);
                        http.connect();

                        if (http.getResponseCode() == 200) {
                            authData = (AuthData) readResponseBody(http, AuthData.class);
                            System.out.printf("New User Created! Logged in as: ", authData.username());
                            setting = "LOGGED_IN";
                        } else {
                            ErrorMessage response = (ErrorMessage) readResponseBody(http, ErrorMessage.class);
                        }
                    } else {
                        throw new Exception("Unknown Command");
                    }
                    break;
                case "LOGGED_IN":
                    if (readEqual(action, "Help")) {
                        System.out.println(helpText(setting));
                    } else if (readEqual(action, "Logout")) {

                    } else if (readEqual(action, "Create")) {

                    } else if (readEqual(action, "List")) {

                    } else if (readEqual(action, "Join")) {

                    } else if (readEqual(action, "Observe")) {

                    } else if (readEqual(action, "Quit")) {
                        //Logout
                        //Quit Game
                        run = false;
                    } else {
                        throw new Exception("Unknown Command");
                    }
                    break;
                case "ingame":
                    //Gameplay code here
                    break;
            }
        }

    }

    private static Boolean readEqual(String input, String test){
        String low = input.toLowerCase();
        String testLow = test.toLowerCase();
        return low.equals(testLow);
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
            responseBody = new Gson().fromJson(inputStreamReader, c);
        }
        return responseBody;
    }

    private static String helpText(String setting){
        if(setting.equals("LOGGED_OUT")){
            StringBuilder builder = new StringBuilder();
            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  register <USERNAME> <PASSWORD> <EMAIL>");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to create an account\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  login <USERNAME> <PASSWORD>");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to login and start playing\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  quit");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to exit\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  help");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to display these command options\n\n");

            return builder.toString();

        } else if (setting.equals("LOGGED_IN")) {
            StringBuilder builder = new StringBuilder();
            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  logout");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to log out of your account\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  create <NAME>");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to create a new game\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  list");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to list all available games\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  join <ID> [WHITE|BLACK|<empty]");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to join the game with that ID as the selected team\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  observe <ID>");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to observe the game with that ID\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  quit");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to exit\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  help");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to display these command options\n\n");

            return builder.toString();
        }else{return null;}
    }
}