package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardPrinter {
    public static void printBoard(ChessBoard board){
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

    public static void printWhite(ChessBoard board){
        //Backwards
        System.out.println(EscapeSequences.SET_TEXT_COLOR_WHITE+EscapeSequences.SET_BG_COLOR_BLACK);
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

    public static void printBlack(ChessBoard board){
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
}
