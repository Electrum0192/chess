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

                } else if (readEqual(action, "Quit")) {
                    run = false;
                } else if (readEqual(action, "Login")) {

                } else if (readEqual(action, "Register")) {

                }else{
                    throw new Exception("Unknown Command");
                }
            } else if (setting.equals("LOGGED_IN")) {
                if(readEqual(action,"Help")){

                } else if (readEqual(action,"Logout")) {

                } else if (readEqual(action,"Create") && readEqual(command[1],"Game")) {

                } else if (readEqual(action,"List") && readEqual(command[1],"Games")) {

                } else if (readEqual(action,"Join") && readEqual(command[1],"Game")) {

                } else if (readEqual(action,"Join") && readEqual(command[1],"Observer")) {

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
}