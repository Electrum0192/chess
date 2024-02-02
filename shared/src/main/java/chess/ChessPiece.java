package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.team = pieceColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return team == that.team && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.team;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Changes the piece type. Used in Pawn Promotion
     *
     * @param type The new type for the piece to promote to
     */
    public void setPieceType(ChessPiece.PieceType type) {
        this.type = type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = null;
        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();
        switch (type){
            case PAWN:{
                FindMoves finder = new PawnMoves();
                if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    moves = finder.whiteMoves(board, myPosition);
                }else{
                    moves = finder.blackMoves(board,myPosition);
                }
                break;
            }
            case ROOK:{
                FindMoves finder = new RookMoves();
                moves = finder.getMoves(board,myPosition);
                break;
            }
            case KNIGHT:{
                FindMoves finder = new KnightMoves();
                moves = finder.getMoves(board,myPosition);
                break;
            }
            case BISHOP:{
                FindMoves finder = new BishopMoves();
                moves = finder.getMoves(board,myPosition);
                break;
            }
            case QUEEN:{
                FindMoves finder = new QueenMoves();
                moves = finder.getMoves(board,myPosition);
                break;
            }
            case KING:{
                FindMoves finder = new KingMoves();
                moves = finder.getMoves(board,myPosition);
                break;
            }
        }
        return moves;
    }

    private ChessGame.TeamColor team;
    private ChessPiece.PieceType type;
}
