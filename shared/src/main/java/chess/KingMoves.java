package chess;

import java.util.Collection;
import java.util.HashSet;

public class KingMoves extends FindMoves {
    public KingMoves() {
        this.moves = new HashSet<ChessMove>();
    }

    @Override
    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition) {
        //Determine Color
        ChessGame.TeamColor enemy;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            enemy = ChessGame.TeamColor.BLACK;
        }else{
            enemy = ChessGame.TeamColor.WHITE;
        }

        //Check Spaces
        checkMove(board, myPosition, new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()), enemy);
        checkMove(board, myPosition, new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1), enemy);
        checkMove(board, myPosition, new ChessPosition(myPosition.getRow(),myPosition.getColumn()+1), enemy);
        checkMove(board, myPosition, new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+1), enemy);
        checkMove(board, myPosition, new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()), enemy);
        checkMove(board, myPosition, new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()-1), enemy);
        checkMove(board, myPosition, new ChessPosition(myPosition.getRow(),myPosition.getColumn()-1), enemy);
        checkMove(board, myPosition, new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-1), enemy);

        return moves;
    }

    private void checkMove(ChessBoard board, ChessPosition myPosition, ChessPosition target, ChessGame.TeamColor enemy){
        boolean isValid = false;
        if(target.getRow() >=1 && target.getColumn() >= 1 && target.getRow() <= 8 && target.getColumn() <= 8){//Check Bounds
            if(board.getPiece(target) == null){//Empty Space
                isValid = true;
            }else if(board.getPiece(target).getTeamColor() == enemy){//Enemy Space
                isValid = true;
            }
        }

        if(isValid){
            ChessMove m = new ChessMove(myPosition,target,null);
            moves.add(m);
        }
    }
    Collection<ChessMove> moves;
}
