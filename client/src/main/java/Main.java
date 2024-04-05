import chess.*;
import com.google.gson.Gson;
import model.*;
import ui.BoardPrinter;
import ui.Client;
import ui.EscapeSequences;
import ui.ServerFacade;
import webSocketMessages.userCommands.*;

import java.util.Collection;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if(args.length == 1){
            serverUrl = args[0];
        }

        System.out.println(EscapeSequences.WHITE_QUEEN+" Welcome to 240 chess. Type \"Help\" to get started. "+EscapeSequences.WHITE_QUEEN);

        try {
            Client.runClient(serverUrl);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}