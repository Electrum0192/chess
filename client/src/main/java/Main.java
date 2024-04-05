import chess.*;
import com.google.gson.Gson;
import model.*;
import ui.EscapeSequences;
import ui.ServerFacade;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
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
            runClient(serverUrl);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private static void runClient(String serverUrl) throws Exception{
        AuthData authData = new AuthData("Null", "Null");
        String team = null;
        int ID = 0;

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
                                System.out.println("Successfully joined game " + command[1] + " as " + command[2]);
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
                                printBlack(facade.getGame().getBoard());
                            }else{printWhite(facade.getGame().getBoard());}
                        } else if (readEqual(action, "Leave")) {
                            Leave leave = new Leave(authData.authToken(),ID);
                            facade.send(new Gson().toJson(leave));
                            setting = "LOGGED_IN";
                            team = null;
                        } else if (readEqual(action, "Move")) {
                            //TODO: parse move from commands
                            MakeMove move = new MakeMove(authData.authToken(),ID,new ChessMove(null,null));
                            facade.send(new Gson().toJson(move));
                        } else if (readEqual(action, "Resign")) {
                            //TODO: Are you sure?
                            Resign resign = new Resign(authData.authToken(),ID);
                            facade.send(new Gson().toJson(resign));
                        } else if (readEqual(action, "Show")) {
                            //TODO: parse position from commands
                            Collection<ChessMove> moves = facade.getGame().validMoves(null);
                            //TODO: change printBoard to show those spaces differently
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
                System.out.println(e.getMessage());
            }
        }

    }

    private static void printBoard(ChessBoard board){
        //Frontwards
        System.out.println(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.println("\u2002 \u2002\u2002H\u2002\u2004\u2004\u2002G\u2002\u2004\u2004\u2002F\u2002\u2004\u2004\u2002E\u2002\u2004\u2004\u2002D\u2002\u2004\u2004\u2002C\u2002\u2004\u2004\u2002B\u2002\u2004\u2004\u2002A");
        for(int r = 1; r <= 8; r++){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
            System.out.print("\u2002"+r+"\u2002");
            for(int c = 1; c <= 8; c++){
                if((r+c)%2 == 0){
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }else{System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);}
                //PRINT PIECE, SET TEXT COLOR TO PIECE TEAM
                ChessPiece piece = board.getPiece(new ChessPosition(r,c));
                if(piece == null){
                    System.out.print(EscapeSequences.EMPTY);
                }else {
                    ChessPiece.PieceType type = piece.getPieceType();
                    if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                        if (type.equals(ChessPiece.PieceType.PAWN)) {
                            System.out.print(EscapeSequences.WHITE_PAWN);
                        } else if (type.equals(ChessPiece.PieceType.ROOK)) {
                            System.out.print(EscapeSequences.WHITE_ROOK);
                        } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
                            System.out.print(EscapeSequences.WHITE_KNIGHT);
                        } else if (type.equals(ChessPiece.PieceType.BISHOP)) {
                            System.out.print(EscapeSequences.WHITE_BISHOP);
                        } else if (type.equals(ChessPiece.PieceType.QUEEN)) {
                            System.out.print(EscapeSequences.WHITE_QUEEN);
                        } else if (type.equals(ChessPiece.PieceType.KING)) {
                            System.out.print(EscapeSequences.WHITE_KING);
                        }
                    } else {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                        if (type.equals(ChessPiece.PieceType.PAWN)) {
                            System.out.print(EscapeSequences.BLACK_PAWN);
                        } else if (type.equals(ChessPiece.PieceType.ROOK)) {
                            System.out.print(EscapeSequences.BLACK_ROOK);
                        } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
                            System.out.print(EscapeSequences.BLACK_KNIGHT);
                        } else if (type.equals(ChessPiece.PieceType.BISHOP)) {
                            System.out.print(EscapeSequences.BLACK_BISHOP);
                        } else if (type.equals(ChessPiece.PieceType.QUEEN)) {
                            System.out.print(EscapeSequences.BLACK_QUEEN);
                        } else if (type.equals(ChessPiece.PieceType.KING)) {
                            System.out.print(EscapeSequences.BLACK_KING);
                        }
                    }
                }
            }
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
            System.out.print("\u2002"+r+"\u2002");
            System.out.println(EscapeSequences.SET_BG_COLOR_BLACK);
        }
        System.out.println("\u2002 \u2002\u2002H\u2002\u2004\u2004\u2002G\u2002\u2004\u2004\u2002F\u2002\u2004\u2004\u2002E\u2002\u2004\u2004\u2002D\u2002\u2004\u2004\u2002C\u2002\u2004\u2004\u2002B\u2002\u2004\u2004\u2002A");

        //Backwards
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.println("\u2002 \u2002\u2002A\u2002\u2004\u2004\u2002B\u2002\u2004\u2004\u2002C\u2002\u2004\u2004\u2002D\u2002\u2004\u2004\u2002E\u2002\u2004\u2004\u2002F\u2002\u2004\u2004\u2002G\u2002\u2004\u2004\u2002H");
        for(int r = 8; r >= 1; r--){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
            System.out.print("\u2002"+r+"\u2002");
            for(int c = 8; c >= 1; c--){
                if((r+c)%2 == 0){
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }else{System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);}
                //PRINT PIECE, SET TEXT COLOR TO PIECE TEAM
                ChessPiece piece = board.getPiece(new ChessPosition(r,c));
                if(piece == null){
                    System.out.print(EscapeSequences.EMPTY);
                }else {
                    ChessPiece.PieceType type = piece.getPieceType();
                    if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                        if (type.equals(ChessPiece.PieceType.PAWN)) {
                            System.out.print(EscapeSequences.WHITE_PAWN);
                        } else if (type.equals(ChessPiece.PieceType.ROOK)) {
                            System.out.print(EscapeSequences.WHITE_ROOK);
                        } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
                            System.out.print(EscapeSequences.WHITE_KNIGHT);
                        } else if (type.equals(ChessPiece.PieceType.BISHOP)) {
                            System.out.print(EscapeSequences.WHITE_BISHOP);
                        } else if (type.equals(ChessPiece.PieceType.QUEEN)) {
                            System.out.print(EscapeSequences.WHITE_QUEEN);
                        } else if (type.equals(ChessPiece.PieceType.KING)) {
                            System.out.print(EscapeSequences.WHITE_KING);
                        }
                    } else {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                        if (type.equals(ChessPiece.PieceType.PAWN)) {
                            System.out.print(EscapeSequences.BLACK_PAWN);
                        } else if (type.equals(ChessPiece.PieceType.ROOK)) {
                            System.out.print(EscapeSequences.BLACK_ROOK);
                        } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
                            System.out.print(EscapeSequences.BLACK_KNIGHT);
                        } else if (type.equals(ChessPiece.PieceType.BISHOP)) {
                            System.out.print(EscapeSequences.BLACK_BISHOP);
                        } else if (type.equals(ChessPiece.PieceType.QUEEN)) {
                            System.out.print(EscapeSequences.BLACK_QUEEN);
                        } else if (type.equals(ChessPiece.PieceType.KING)) {
                            System.out.print(EscapeSequences.BLACK_KING);
                        }
                    }
                }
            }
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
            System.out.print("\u2002"+r+"\u2002");
            System.out.println(EscapeSequences.SET_BG_COLOR_BLACK);
        }
        System.out.println("\u2002 \u2002\u2002A\u2002\u2004\u2004\u2002B\u2002\u2004\u2004\u2002C\u2002\u2004\u2004\u2002D\u2002\u2004\u2004\u2002E\u2002\u2004\u2004\u2002F\u2002\u2004\u2004\u2002G\u2002\u2004\u2004\u2002H");

        //Reset Color Stuff
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void printWhite(ChessBoard board){
        //Backwards
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.println("\u2002 \u2002\u2002A\u2002\u2004\u2004\u2002B\u2002\u2004\u2004\u2002C\u2002\u2004\u2004\u2002D\u2002\u2004\u2004\u2002E\u2002\u2004\u2004\u2002F\u2002\u2004\u2004\u2002G\u2002\u2004\u2004\u2002H");
        for(int r = 8; r >= 1; r--){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
            System.out.print("\u2002"+r+"\u2002");
            for(int c = 8; c >= 1; c--){
                if((r+c)%2 == 0){
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }else{System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);}
                //PRINT PIECE, SET TEXT COLOR TO PIECE TEAM
                ChessPiece piece = board.getPiece(new ChessPosition(r,c));
                if(piece == null){
                    System.out.print(EscapeSequences.EMPTY);
                }else {
                    ChessPiece.PieceType type = piece.getPieceType();
                    if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                        if (type.equals(ChessPiece.PieceType.PAWN)) {
                            System.out.print(EscapeSequences.WHITE_PAWN);
                        } else if (type.equals(ChessPiece.PieceType.ROOK)) {
                            System.out.print(EscapeSequences.WHITE_ROOK);
                        } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
                            System.out.print(EscapeSequences.WHITE_KNIGHT);
                        } else if (type.equals(ChessPiece.PieceType.BISHOP)) {
                            System.out.print(EscapeSequences.WHITE_BISHOP);
                        } else if (type.equals(ChessPiece.PieceType.QUEEN)) {
                            System.out.print(EscapeSequences.WHITE_QUEEN);
                        } else if (type.equals(ChessPiece.PieceType.KING)) {
                            System.out.print(EscapeSequences.WHITE_KING);
                        }
                    } else {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                        if (type.equals(ChessPiece.PieceType.PAWN)) {
                            System.out.print(EscapeSequences.BLACK_PAWN);
                        } else if (type.equals(ChessPiece.PieceType.ROOK)) {
                            System.out.print(EscapeSequences.BLACK_ROOK);
                        } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
                            System.out.print(EscapeSequences.BLACK_KNIGHT);
                        } else if (type.equals(ChessPiece.PieceType.BISHOP)) {
                            System.out.print(EscapeSequences.BLACK_BISHOP);
                        } else if (type.equals(ChessPiece.PieceType.QUEEN)) {
                            System.out.print(EscapeSequences.BLACK_QUEEN);
                        } else if (type.equals(ChessPiece.PieceType.KING)) {
                            System.out.print(EscapeSequences.BLACK_KING);
                        }
                    }
                }
            }
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
            System.out.print("\u2002"+r+"\u2002");
            System.out.println(EscapeSequences.SET_BG_COLOR_BLACK);
        }
        System.out.println("\u2002 \u2002\u2002A\u2002\u2004\u2004\u2002B\u2002\u2004\u2004\u2002C\u2002\u2004\u2004\u2002D\u2002\u2004\u2004\u2002E\u2002\u2004\u2004\u2002F\u2002\u2004\u2004\u2002G\u2002\u2004\u2004\u2002H");


        //Reset Color Stuff
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
    }

    private static void printBlack(ChessBoard board){
        //Frontwards
        System.out.println(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.println("\u2002 \u2002\u2002H\u2002\u2004\u2004\u2002G\u2002\u2004\u2004\u2002F\u2002\u2004\u2004\u2002E\u2002\u2004\u2004\u2002D\u2002\u2004\u2004\u2002C\u2002\u2004\u2004\u2002B\u2002\u2004\u2004\u2002A");
        for(int r = 1; r <= 8; r++){
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
            System.out.print("\u2002"+r+"\u2002");
            for(int c = 1; c <= 8; c++){
                if((r+c)%2 == 0){
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }else{System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);}
                //PRINT PIECE, SET TEXT COLOR TO PIECE TEAM
                ChessPiece piece = board.getPiece(new ChessPosition(r,c));
                if(piece == null){
                    System.out.print(EscapeSequences.EMPTY);
                }else {
                    ChessPiece.PieceType type = piece.getPieceType();
                    if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
                        if (type.equals(ChessPiece.PieceType.PAWN)) {
                            System.out.print(EscapeSequences.WHITE_PAWN);
                        } else if (type.equals(ChessPiece.PieceType.ROOK)) {
                            System.out.print(EscapeSequences.WHITE_ROOK);
                        } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
                            System.out.print(EscapeSequences.WHITE_KNIGHT);
                        } else if (type.equals(ChessPiece.PieceType.BISHOP)) {
                            System.out.print(EscapeSequences.WHITE_BISHOP);
                        } else if (type.equals(ChessPiece.PieceType.QUEEN)) {
                            System.out.print(EscapeSequences.WHITE_QUEEN);
                        } else if (type.equals(ChessPiece.PieceType.KING)) {
                            System.out.print(EscapeSequences.WHITE_KING);
                        }
                    } else {
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
                        if (type.equals(ChessPiece.PieceType.PAWN)) {
                            System.out.print(EscapeSequences.BLACK_PAWN);
                        } else if (type.equals(ChessPiece.PieceType.ROOK)) {
                            System.out.print(EscapeSequences.BLACK_ROOK);
                        } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
                            System.out.print(EscapeSequences.BLACK_KNIGHT);
                        } else if (type.equals(ChessPiece.PieceType.BISHOP)) {
                            System.out.print(EscapeSequences.BLACK_BISHOP);
                        } else if (type.equals(ChessPiece.PieceType.QUEEN)) {
                            System.out.print(EscapeSequences.BLACK_QUEEN);
                        } else if (type.equals(ChessPiece.PieceType.KING)) {
                            System.out.print(EscapeSequences.BLACK_KING);
                        }
                    }
                }
            }
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
            System.out.print("\u2002"+r+"\u2002");
            System.out.println(EscapeSequences.SET_BG_COLOR_BLACK);
        }
        System.out.println("\u2002 \u2002\u2002H\u2002\u2004\u2004\u2002G\u2002\u2004\u2004\u2002F\u2002\u2004\u2004\u2002E\u2002\u2004\u2004\u2002D\u2002\u2004\u2004\u2002C\u2002\u2004\u2004\u2002B\u2002\u2004\u2004\u2002A");


        //Reset Color Stuff
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
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