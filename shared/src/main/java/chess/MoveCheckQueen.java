package chess;

import java.util.Collection;
import java.util.HashSet;

public class MoveCheckQueen extends MoveCheck{
    public MoveCheckQueen() {
        this.moves = new HashSet<ChessMove>();
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        //Determine Color
        ChessGame.TeamColor enemy;
        if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            enemy = ChessGame.TeamColor.WHITE;
        }else{enemy = ChessGame.TeamColor.BLACK;}

        //Calculate Moves
        //Diagonal
        recurseMove(board, myPosition, myPosition, -1, -1, enemy);
        recurseMove(board, myPosition, myPosition, 1, -1, enemy);
        recurseMove(board, myPosition, myPosition, -1, 1, enemy);
        recurseMove(board, myPosition, myPosition, 1, 1, enemy);
        //Lateral
        recurseMove(board, myPosition, myPosition, -1, 0, enemy);
        recurseMove(board, myPosition, myPosition, 1, 0, enemy);
        recurseMove(board, myPosition, myPosition, 0, 1, enemy);
        recurseMove(board, myPosition, myPosition, 0, -1, enemy);

        return moves;
    }

    private void recurseMove(ChessBoard board, ChessPosition start, ChessPosition position, int rowShift, int columnShift, ChessGame.TeamColor enemy) {
        boolean isValid = false;
        boolean capture = false;
        ChessPosition target = new ChessPosition(position.getRow()+rowShift, position.getColumn()+columnShift);

        //Check this space
        if(target.getRow() == 0 || target.getColumn() == 0 || target.getColumn() == 9 || target.getRow() == 9){ //In Bounds
            isValid = false;
        }else if(board.getPiece(target) == null){ //Open Space
            isValid = true;
        }else if(board.getPiece(target).getTeamColor() == enemy){ //Capture
            isValid = true;
            capture = true;
        }

        //Add move
        if(isValid){
            ChessMove queenMove = new ChessMove(start, target, null);
            moves.add(queenMove);
        }
        //Check next space
        if(isValid && !capture){
            recurseMove(board, start, target, rowShift, columnShift, enemy);
        }
    }

    private Collection<ChessMove> moves;
}
