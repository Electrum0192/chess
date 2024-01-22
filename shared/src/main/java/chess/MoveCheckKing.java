package chess;

import java.util.Collection;
import java.util.HashSet;

public class MoveCheckKing extends MoveCheck{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        //Determine Color
        ChessGame.TeamColor enemy;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            enemy = ChessGame.TeamColor.WHITE;
        }else{enemy = ChessGame.TeamColor.BLACK;}

        //Check adjacent spaces
        ChessPosition tempPos = new ChessPosition(r+1,c);
        if(r != 8) {
            if (board.getPiece(tempPos) == null) {
                ChessMove up = new ChessMove(myPosition, tempPos, null);
                moves.add(up);
            } else if (board.getPiece(tempPos).getTeamColor() == enemy) {
                ChessMove up = new ChessMove(myPosition, tempPos, null);
                moves.add(up);
            }
        }

        tempPos = new ChessPosition(r+1,c+1);
        if(r != 8 && c != 8) {
            if (board.getPiece(tempPos) == null) {
                ChessMove upRight = new ChessMove(myPosition, tempPos, null);
                moves.add(upRight);
            } else if (board.getPiece(tempPos).getTeamColor() == enemy) {
                ChessMove upRight = new ChessMove(myPosition, tempPos, null);
                moves.add(upRight);
            }
        }

        tempPos = new ChessPosition(r,c+1);
        if(c != 8) {
            if (board.getPiece(tempPos) == null) {
                ChessMove right = new ChessMove(myPosition, tempPos, null);
                moves.add(right);
            } else if (board.getPiece(tempPos).getTeamColor() == enemy) {
                ChessMove right = new ChessMove(myPosition, tempPos, null);
                moves.add(right);
            }
        }

        tempPos = new ChessPosition(r-1,c+1);
        if(r != 1 && c != 8) {
            if (board.getPiece(tempPos) == null) {
                ChessMove downRight = new ChessMove(myPosition, tempPos, null);
                moves.add(downRight);
            } else if (board.getPiece(tempPos).getTeamColor() == enemy) {
                ChessMove downRight = new ChessMove(myPosition, tempPos, null);
                moves.add(downRight);
            }
        }

        tempPos = new ChessPosition(r-1,c);
        if(r != 1) {
            if (board.getPiece(tempPos) == null) {
                ChessMove down = new ChessMove(myPosition, tempPos, null);
                moves.add(down);
            } else if (board.getPiece(tempPos).getTeamColor() == enemy) {
                ChessMove down = new ChessMove(myPosition, tempPos, null);
                moves.add(down);
            }
        }

        tempPos = new ChessPosition(r-1,c-1);
        if(r != 1 && c != 1) {
            if (board.getPiece(tempPos) == null) {
                ChessMove downLeft = new ChessMove(myPosition, tempPos, null);
                moves.add(downLeft);
            } else if (board.getPiece(tempPos).getTeamColor() == enemy) {
                ChessMove downLeft = new ChessMove(myPosition, tempPos, null);
                moves.add(downLeft);
            }
        }

        tempPos = new ChessPosition(r,c-1);
        if(c != 1) {
            if (board.getPiece(tempPos) == null) {
                ChessMove left = new ChessMove(myPosition, tempPos, null);
                moves.add(left);
            } else if (board.getPiece(tempPos).getTeamColor() == enemy) {
                ChessMove left = new ChessMove(myPosition, tempPos, null);
                moves.add(left);
            }
        }

        tempPos = new ChessPosition(r+1,c-1);
        if(r != 8 && c != 1) {
            if (board.getPiece(tempPos) == null) {
                ChessMove upLeft = new ChessMove(myPosition, tempPos, null);
                moves.add(upLeft);
            } else if (board.getPiece(tempPos).getTeamColor() == enemy) {
                ChessMove upLeft = new ChessMove(myPosition, tempPos, null);
                moves.add(upLeft);
            }
        }

        return moves;
    }
}
