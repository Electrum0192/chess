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
}
