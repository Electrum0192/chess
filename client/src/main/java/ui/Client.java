package ui;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.AuthData;
import model.Game;
import model.GameList;
import model.UserData;
import webSocketMessages.userCommands.Leave;
import webSocketMessages.userCommands.MakeMove;
import webSocketMessages.userCommands.Resign;

import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

public class Client {


    private static AuthData authData;
    private static String team;
    private static int ID;

    private static final Client instance = new Client();
    public static Client getInstance(){return instance;}

    public Client() {
        authData = new AuthData("null","null");
        team = null;
        ID = 0;
    }

    public static void runClient(String serverUrl) throws Exception{

        ServerFacade facade = new ServerFacade();
        facade.connect(serverUrl);

        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);

        //Loop for user input/client output
        boolean run = true;
        String setting = "LOGGED_OUT";
        while(run){



            //if(authData != null){System.out.println(authData.toString());} //For Debugging
            //Get user input
            if(!setting.equals("ingame")){
                System.out.printf("[%s] >>> ",setting);
            }else{System.out.printf("[%s] >>> ",team);}
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var command = line.split(" ");
            String action = command[0];

            //Read command and create the http request
            try {
                switch (setting) {
                    case "LOGGED_OUT":
                        if (readEqual(action, "Help")) {
                            System.out.println(helpText(setting));
                        } else if (readEqual(action, "Quit")) {
                            System.out.println("Goodbye");
                            run = false;
                        } else if (readEqual(action, "Login")) {
                            authData = ServerFacade.login(serverUrl, new UserData(command[1],command[2],null));
                            System.out.printf("Logged in as: %s\n", authData.username());
                            setting = "LOGGED_IN";

                        } else if (readEqual(action, "Register")) {
                            UserData newUser = new UserData(command[1],command[2],command[3]);
                            authData = ServerFacade.register(serverUrl,newUser);
                            System.out.printf("New User Created! Logged in as: %s\n", authData.username());
                            setting = "LOGGED_IN";

                        } else if (readEqual(action,"Clear")) { //Secret, password protected method for clearing the Database
                            if(command[1].equals("wotsirb123")) {
                                ServerFacade.clear(serverUrl);
                                System.out.println("Database cleared");
                            }
                        } else {
                            throw new Exception("Unknown Command");
                        }
                        break;
                    case "LOGGED_IN":
                        if (readEqual(action, "Help")) {
                            System.out.println(helpText(setting));
                        } else if (readEqual(action, "Logout")) {
                            if (ServerFacade.logout(serverUrl,authData.authToken())) {
                                System.out.println(authData.username() + " has logged out. Thanks for playing!");
                                authData = null;
                                setting = "LOGGED_OUT";
                            }
                        } else if (readEqual(action, "Create")) {
                            int gameID = ServerFacade.create(serverUrl,authData.authToken(),command[1]);
                            System.out.printf("New game created with ID: %d\n", gameID);

                        } else if (readEqual(action, "List")) {
                            printList(ServerFacade.list(serverUrl,authData.authToken()));

                        } else if (readEqual(action, "Join")) {
                            if(command.length==3) {
                                var joinPlayer = ServerFacade.join(serverUrl, authData.authToken(), Integer.parseInt(command[1]), command[2]);
                                facade.send(new Gson().toJson(joinPlayer));
                                System.out.println("Loading game:");
                                ID = Integer.parseInt(command[1]);
                                setting = "ingame";
                                team = command[2].toUpperCase();
                            }else{
                                //requestColor = empty aka null
                                var joinObserver = ServerFacade.join(serverUrl,authData.authToken(),Integer.parseInt(command[1]),"null");
                                facade.send(new Gson().toJson(joinObserver));
                                System.out.printf("Now observing game #%s.\n",command[1]);
                                System.out.println("Loading game:");
                                ID = Integer.parseInt(command[1]);
                                setting = "ingame";
                                team = "OBSERVER";
                            }

                        } else if (readEqual(action, "Observe")) {
                            var joinObserver = ServerFacade.join(serverUrl,authData.authToken(),Integer.parseInt(command[1]),"null");
                            facade.send(new Gson().toJson(joinObserver));
                            System.out.printf("Now observing game #%s.\n",command[1]);
                            System.out.println("Loading game:");
                            ID = Integer.parseInt(command[1]);
                            setting = "ingame";
                            team = "OBSERVER";

                        } else if (readEqual(action, "Quit")) {
                            ServerFacade.logout(serverUrl,authData.authToken());
                            System.out.println("Goodbye");
                            run = false;
                        } else if (readEqual(action,"Clear")) { //Secret, password protected method for clearing the Database
                            if(command[1].equals("wotsirb123")) {
                                ServerFacade.clear(serverUrl);
                                System.out.println("Database cleared");

                            }
                        } else {
                            throw new Exception("Unknown Command");
                        }
                        break;
                    case "ingame":
                        if (readEqual(action, "Help")) {
                            System.out.println(helpText(setting));
                        } else if (readEqual(action, "Draw")) {
                            if(team.equals("BLACK")){
                                BoardPrinter.printBlack(facade.getGame().getBoard(), null);
                            }else{BoardPrinter.printWhite(facade.getGame().getBoard(), null);}
                        } else if (readEqual(action, "Leave")) {
                            Leave leave = new Leave(authData.authToken(),ID);
                            facade.send(new Gson().toJson(leave));
                            setting = "LOGGED_IN";
                            team = null;
                            ID = 0;
                        } else if (readEqual(action, "Move")) {
                            ChessMove newMove = new ChessMove(parsePos(command[1]),parsePos(command[2]));
                            MakeMove move = new MakeMove(authData.authToken(),ID,newMove);
                            facade.send(new Gson().toJson(move));
                        } else if (readEqual(action, "Resign")) {
                            System.out.println("Please confirm that you would like to concede the game (Y/N):");
                            String confirm = scanner.nextLine();
                            if(readEqual(confirm,"Y") || readEqual(confirm, "Yes")) {
                                Resign resign = new Resign(authData.authToken(), ID);
                                facade.send(new Gson().toJson(resign));
                                setting = "LOGGED_IN";
                                team = null;
                                ID = 0;
                            }else{
                                System.out.println("Resignation cancelled");
                            }
                        } else if (readEqual(action, "Show")) {
                            Collection<ChessMove> moves = facade.getGame().validMoves(parsePos(command[1]));
                            if(team.equals("BLACK")){
                                BoardPrinter.printBlack(facade.getGame().getBoard(), (HashSet<ChessMove>) moves);
                            }else{
                                BoardPrinter.printWhite(facade.getGame().getBoard(), (HashSet<ChessMove>) moves);
                            }
                        } else if (readEqual(action,"Clear")) { //Secret, password protected method for clearing the Database
                            if(command[1].equals("wotsirb123")) {
                                ServerFacade.clear(serverUrl);
                                System.out.println("Database cleared");

                            }
                        } else {
                            throw new Exception("Unknown Command");
                        }
                        break;
                }
            }catch (Exception e){
                try{
                    ServerFacade.readError(Integer.parseInt(e.getMessage()));
                }catch (Exception e2){
                    System.out.println(e.getMessage());
                }
            }
        }

    }

    public static String getTeam(){
        return team;
    }

    private static void printList(GameList list){
        Collection<Game> games = list.games();
        System.out.printf("Found %d game(s):\n",games.size());
        boolean flip = true;
        for(var i : games){
            if(flip){
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
                flip = false;
            }else{
                System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
                flip = true;
            }
            StringBuilder builder = new StringBuilder();
            builder.append(i.gameID());
            builder.append(" | ");
            builder.append(i.gameName());
            builder.append(" | W: ");
            builder.append(i.whiteUsername());
            builder.append(" | B: ");
            builder.append(i.blackUsername());
            System.out.println(builder.toString());
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static Boolean readEqual(String input, String test){
        String low = input.toLowerCase();
        String testLow = test.toLowerCase();
        return low.equals(testLow);
    }

    private static ChessPosition parsePos(String in) throws Exception {
        if(in.length() != 2){
            throw new Exception("Invalid coordinate");
        }
        in = in.toUpperCase();

        int row = Integer.parseInt(in.substring(1));
        int column = 0;
        switch (in.charAt(0)) {
            case 'A' -> column = 8;
            case 'B' -> column = 7;
            case 'C' -> column = 6;
            case 'D' -> column = 5;
            case 'E' -> column = 4;
            case 'F' -> column = 3;
            case 'G' -> column = 2;
            case 'H' -> column = 1;
        }
        if(column == 0){
            throw new Exception("Invalid coordinate");
        }
        return new ChessPosition(row, column);
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
        } else if (setting.equals("ingame")) {
            StringBuilder builder = new StringBuilder();
            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  draw");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to redraw the chessboard\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  leave");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to exit the game and go back to the menu\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  move <Position> <Position>");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to move a piece from the first position to the second. Example: move D2 D4\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  Resign");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to declare the other player as the winner and end the game\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  Show <Position>");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to show the legal moves the piece at that position can make\n");

            builder.append(EscapeSequences.SET_TEXT_COLOR_BLUE);
            builder.append("  help");
            builder.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
            builder.append(" - to display these command options\n\n");

            return builder.toString();
        }else{return null;}
    }
}
