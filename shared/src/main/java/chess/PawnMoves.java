package chess;

import java.util.Collection;
import java.util.HashSet;

public class PawnMoves extends FindMoves {
    public PawnMoves() {
        this.moves = new HashSet<ChessMove>();
    }

    public Collection<ChessMove> whiteMoves(ChessBoard board, ChessPosition myPosition){
        if(myPosition.getRow() != 8) {
            //Forward
            ChessPosition oneSpace = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (board.getPiece(oneSpace) == null) {
                if (myPosition.getRow() == 7) {
                    addPromoMoves(myPosition,oneSpace);
                } else {
                    ChessMove forward = new ChessMove(myPosition, oneSpace, null);
                    moves.add(forward);
                }
            }
            //Dash
            if(myPosition.getRow() == 2) {
                ChessPosition twoSpace = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                if (board.getPiece(oneSpace) == null && board.getPiece(twoSpace) == null) {
                    ChessMove dash = new ChessMove(myPosition, twoSpace, null);
                    moves.add(dash);
                }
            }
            //Left Capture
            if(myPosition.getColumn() != 1) {
                ChessPosition upLeft = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                if (board.getPiece(upLeft) != null) {
                    if (board.getPiece(upLeft).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if(myPosition.getRow() == 7){
                            ChessMove capLeftRook = new ChessMove(myPosition, upLeft, ChessPiece.PieceType.ROOK);
                            moves.add(capLeftRook);
                            ChessMove capLeftKnight = new ChessMove(myPosition, upLeft, ChessPiece.PieceType.KNIGHT);
                            moves.add(capLeftKnight);
                            ChessMove capLeftBishop = new ChessMove(myPosition, upLeft, ChessPiece.PieceType.BISHOP);
                            moves.add(capLeftBishop);
                            ChessMove capLeftQueen = new ChessMove(myPosition, upLeft, ChessPiece.PieceType.QUEEN);
                            moves.add(capLeftQueen);
                        }else {
                            ChessMove capLeft = new ChessMove(myPosition, upLeft, null);
                            moves.add(capLeft);
                        }
                    }
                }
            }
            //Right Capture
            if(myPosition.getColumn() != 8) {
                ChessPosition upRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                if (board.getPiece(upRight) != null) {
                    if (board.getPiece(upRight).getTeamColor() == ChessGame.TeamColor.BLACK) {
                        if(myPosition.getRow() == 7){
                            ChessMove capRightRook = new ChessMove(myPosition, upRight, ChessPiece.PieceType.ROOK);
                            moves.add(capRightRook);
                            ChessMove capRightKnight = new ChessMove(myPosition, upRight, ChessPiece.PieceType.KNIGHT);
                            moves.add(capRightKnight);
                            ChessMove capRightBishop = new ChessMove(myPosition, upRight, ChessPiece.PieceType.BISHOP);
                            moves.add(capRightBishop);
                            ChessMove capRightQueen = new ChessMove(myPosition, upRight, ChessPiece.PieceType.QUEEN);
                            moves.add(capRightQueen);
                        }else {
                            ChessMove capRight = new ChessMove(myPosition, upRight, null);
                            moves.add(capRight);
                        }
                    }
                }
            }
            //Left En Passant
            if(myPosition.getColumn() != 1) {
                ChessPosition upLeft = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                ChessPosition left = new ChessPosition(myPosition.getRow(),myPosition.getColumn()-1);
                if (board.getPiece(left) != null) {
                    if (board.getPiece(left).getPassant()) {
                        //System.out.println("FLAG 1");
                        ChessMove capLeft = new ChessMove(myPosition, upLeft, null);
                        moves.add(capLeft);
                    }
                }
            }
            //Right En Passant
            if(myPosition.getColumn() != 8) {
                ChessPosition upRight = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                ChessPosition right = new ChessPosition(myPosition.getRow(),myPosition.getColumn()+1);
                if (board.getPiece(right) != null) {
                    if (board.getPiece(right).getPassant()) {
                        //System.out.println("FLAG 2");
                        ChessMove capRight = new ChessMove(myPosition, upRight, null);
                        moves.add(capRight);
                    }
                }
            }
        }

        return moves;
    }
    public Collection<ChessMove> blackMoves(ChessBoard board, ChessPosition myPosition){
        if(myPosition.getRow() != 1) {
            //Forward
            ChessPosition oneSpace = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if (board.getPiece(oneSpace) == null) {
                if (myPosition.getRow() == 2) {
                    addPromoMoves(myPosition, oneSpace);
                } else {
                    ChessMove forward = new ChessMove(myPosition, oneSpace, null);
                    moves.add(forward);
                }
            }
            //Dash
            if(myPosition.getRow() == 7) {
                ChessPosition twoSpace = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                if (board.getPiece(oneSpace) == null && board.getPiece(twoSpace) == null) {
                    ChessMove dash = new ChessMove(myPosition, twoSpace, null);
                    moves.add(dash);
                }
            }
            //Left Capture
            if(myPosition.getColumn() != 1) {
                ChessPosition upLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                if (board.getPiece(upLeft) != null) {
                    if (board.getPiece(upLeft).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        if(myPosition.getRow() == 2){
                            ChessMove capLeftRook = new ChessMove(myPosition, upLeft, ChessPiece.PieceType.ROOK);
                            moves.add(capLeftRook);
                            ChessMove capLeftKnight = new ChessMove(myPosition, upLeft, ChessPiece.PieceType.KNIGHT);
                            moves.add(capLeftKnight);
                            ChessMove capLeftBishop = new ChessMove(myPosition, upLeft, ChessPiece.PieceType.BISHOP);
                            moves.add(capLeftBishop);
                            ChessMove capLeftQueen = new ChessMove(myPosition, upLeft, ChessPiece.PieceType.QUEEN);
                            moves.add(capLeftQueen);
                        }else {
                            ChessMove capLeft = new ChessMove(myPosition, upLeft, null);
                            moves.add(capLeft);
                        }
                    }
                }
            }
            //Right Capture
            if(myPosition.getColumn() != 8) {
                ChessPosition upRight = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                if (board.getPiece(upRight) != null) {
                    if (board.getPiece(upRight).getTeamColor() == ChessGame.TeamColor.WHITE) {
                        if(myPosition.getRow() == 2){
                            ChessMove capRightRook = new ChessMove(myPosition, upRight, ChessPiece.PieceType.ROOK);
                            moves.add(capRightRook);
                            ChessMove capRightKnight = new ChessMove(myPosition, upRight, ChessPiece.PieceType.KNIGHT);
                            moves.add(capRightKnight);
                            ChessMove capRightBishop = new ChessMove(myPosition, upRight, ChessPiece.PieceType.BISHOP);
                            moves.add(capRightBishop);
                            ChessMove capRightQueen = new ChessMove(myPosition, upRight, ChessPiece.PieceType.QUEEN);
                            moves.add(capRightQueen);
                        }else {
                            ChessMove capRight = new ChessMove(myPosition, upRight, null);
                            moves.add(capRight);
                        }
                    }
                }
            }
            //Left En Passant
            if(myPosition.getColumn() != 1) {
                ChessPosition upLeft = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                ChessPosition left = new ChessPosition(myPosition.getRow(),myPosition.getColumn()-1);
                if (board.getPiece(left) != null) {
                    if (board.getPiece(left).getPassant()) {
                        //System.out.println("FLAG 3");
                        ChessMove capLeft = new ChessMove(myPosition, upLeft, null);
                        moves.add(capLeft);
                    }
                }
            }
            //Right En Passant
            if(myPosition.getColumn() != 8) {
                ChessPosition upRight = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                ChessPosition right = new ChessPosition(myPosition.getRow(),myPosition.getColumn()+1);
                if (board.getPiece(right) != null) {
                    if (board.getPiece(right).getPassant()) {
                        //System.out.println("FLAG 4");
                        ChessMove capRight = new ChessMove(myPosition, upRight, null);
                        moves.add(capRight);
                    }
                }
            }
        }

        return moves;
    }

    private void addPromoMoves(ChessPosition myPosition, ChessPosition oneSpace){
        ChessMove promoteRook = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.ROOK);
        ChessMove promoteKnight = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.KNIGHT);
        ChessMove promoteQueen = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.QUEEN);
        ChessMove promoteBishop = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.BISHOP);
        moves.add(promoteRook);
        moves.add(promoteKnight);
        moves.add(promoteQueen);
        moves.add(promoteBishop);
    }
    Collection<ChessMove> moves;
}
