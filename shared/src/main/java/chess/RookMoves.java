package chess;

import java.util.Collection;
import java.util.HashSet;

public class RookMoves extends FindMoves {
    public RookMoves() {
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

        return moves;
    }


    Collection<ChessMove> moves;
}
