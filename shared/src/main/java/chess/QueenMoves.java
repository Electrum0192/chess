package chess;

import java.util.Collection;

public class QueenMoves extends FindMoves {
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

        return this.returnMoves();
    }

    Collection<ChessMove> moves;
}
