package chess;

import java.util.Collection;
import java.util.HashSet;

public class QueenMoves extends FindMoves {
    public QueenMoves() {
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
        recurseMove(board,myPosition,myPosition,1,0,enemy);
        recurseMove(board,myPosition,myPosition,0,1,enemy);
        recurseMove(board,myPosition,myPosition,-1,0,enemy);
        recurseMove(board,myPosition,myPosition,0,-1,enemy);
        recurseMove(board,myPosition,myPosition,1,1,enemy);
        recurseMove(board,myPosition,myPosition,1,-1,enemy);
        recurseMove(board,myPosition,myPosition,-1,1,enemy);
        recurseMove(board,myPosition,myPosition,-1,-1,enemy);

        return moves;
    }

    private void recurseMove(ChessBoard board, ChessPosition myPosition, ChessPosition curPosition, int up, int right, ChessGame.TeamColor enemy){
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

    Collection<ChessMove> moves;
}
