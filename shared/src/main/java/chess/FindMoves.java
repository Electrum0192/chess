package chess;

import java.util.Collection;
import java.util.HashSet;

public class FindMoves {
    public FindMoves() {
        moves = new HashSet<ChessMove>();
    }

    public Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition){
        return moves;
    }
    public Collection<ChessMove> whiteMoves(ChessBoard board, ChessPosition myPosition){
        return moves;
    }
    public Collection<ChessMove> blackMoves(ChessBoard board, ChessPosition myPosition){
        return moves;
    }
    private Collection<ChessMove> moves;

    void recurseMove(ChessBoard board, ChessPosition myPosition, ChessPosition curPosition, int up, int right, ChessGame.TeamColor enemy){
        ChessPosition target = new ChessPosition(curPosition.getRow()+up,curPosition.getColumn()+right);
        boolean repeat = checkMove(board,myPosition,target,enemy);

        if(repeat){
            recurseMove(board,myPosition,target,up,right,enemy);
        }
    }

    private boolean checkMove(ChessBoard board, ChessPosition myPosition, ChessPosition target, ChessGame.TeamColor enemy){
        boolean isValid = false;
        boolean capture = false;
        if(target.getRow() >=1 && target.getColumn() >= 1 && target.getRow() <= 8 && target.getColumn() <= 8){//Check Bounds
            if(board.getPiece(target) == null){//Empty Space
                isValid = true;
            }else if(board.getPiece(target).getTeamColor() == enemy){//Enemy Space
                capture = true;
            }
        }

        if(isValid || capture){
            ChessMove m = new ChessMove(myPosition,target,null);
            moves.add(m);
        }

        return isValid;
    }
}
