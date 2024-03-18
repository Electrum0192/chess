import chess.*;
import ui.EscapeSequences;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if(args.length == 1){
            serverUrl = args[0];
        }

        System.out.println(EscapeSequences.WHITE_QUEEN+" Welcome to 240 chess. Type \"Help\" to get started. "+EscapeSequences.WHITE_QUEEN);

        try {
            connect(serverUrl);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void connect(String serverUrl) throws Exception{
        //Set up server connection
        URI uri = new URI(serverUrl);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();

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
            if(setting.equals("LOGGED_OUT")) {
                if (readEqual(action, "Help")) {
                    System.out.println(helpText(setting));
                } else if (readEqual(action, "Quit")) {
                    run = false;
                } else if (readEqual(action, "Login")) {

                } else if (readEqual(action, "Register")) {

                }else{
                    throw new Exception("Unknown Command");
                }
            } else if (setting.equals("LOGGED_IN")) {
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
                }else{
                    throw new Exception("Unknown Command");
                }
            }else if (setting.equals("ingame")){
                //Gameplay code here
            }
        }

    }

    private static Boolean readEqual(String input, String test){
        String low = input.toLowerCase();
        String testLow = test.toLowerCase();
        return low.equals(testLow);
    }

    private static String helpText(String setting){
        if(setting.equals("LOGGED_OUT")){
            StringBuilder builder = new StringBuilder();
            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  register <USERNAME> <PASSWORD> <EMAIL>");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to create an account\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  login <USERNAME> <PASSWORD>");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to login and start playing\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  quit");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to exit\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  help");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to display these command options\n\n");

            return builder.toString();

        } else if (setting.equals("LOGGED_IN")) {
            StringBuilder builder = new StringBuilder();
            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  logout");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to log out of your account\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  create <NAME>");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to create a new game\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  list");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to list all available games\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  join <ID> [WHITE|BLACK|<empty]");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to join the game with that ID as the selected team\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  observe <ID>");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to observe the game with that ID\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  quit");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to exit\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  help");
            builder.append(EscapeSequences.RESET_TEXT_COLOR);
            builder.append(" - to display these command options\n\n");

            return builder.toString();
        }
    }
}