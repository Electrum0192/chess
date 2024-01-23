package chess;

import java.util.Collection;
import java.util.HashSet;

public class MoveCheckKnight extends MoveCheck{
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

        //Calculate Moves
        ChessPosition spaces[] = {new ChessPosition(r-2,c-1),new ChessPosition(r-1,c-2),
                new ChessPosition(r+2,c-1),new ChessPosition(r+1,c-2),
                new ChessPosition(r-2,c+1),new ChessPosition(r-1,c+2),
                new ChessPosition(r+2,c+1),new ChessPosition(r+1,c+2),};
        for(int a = 0; a < spaces.length; a++){
            if(isValidMove(board,spaces[a],enemy)){
                ChessMove knightMove = new ChessMove(myPosition,spaces[a],null);
                moves.add(knightMove);
            }
        }

        return moves;
    }

    private boolean isValidMove(ChessBoard board, ChessPosition target, ChessGame.TeamColor enemy) {
        if(target.getRow() < 1 || target.getRow() > 8 || target.getColumn() < 1 || target.getColumn() > 8){ //In Bounds
            return false;
        }else if(board.getPiece(target) == null){ //Open Space
            return true;
        }else if(board.getPiece(target).getTeamColor() == enemy){ //Capture
            return true;
        }

        return false;
    }
}
