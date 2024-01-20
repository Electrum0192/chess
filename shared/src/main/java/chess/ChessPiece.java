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
        this.color = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
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
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //Get Piece in that position on that board
        ChessPiece.PieceType type = board.getPiece(myPosition).getPieceType();
        //Call MoveCheck of that type to get collection
        Collection<ChessMove> moves = null;
        switch (type){
            case PAWN:
                MoveCheck checkPawn = new MoveCheckPawn();
                if(board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    moves = checkPawn.whiteMoves(board, myPosition);
                }else{
                    moves = checkPawn.blackMoves(board, myPosition);
                }
                break;
            case BISHOP:
                MoveCheck checkBishop = new MoveCheckBishop();
                moves = checkBishop.pieceMoves(board, myPosition);
                break;
            case ROOK:
                MoveCheck checkRook = new MoveCheckRook();
                moves = checkRook.pieceMoves(board, myPosition);
                break;
            case KNIGHT:
                MoveCheck checkKnight = new MoveCheckKnight();
                moves = checkKnight.pieceMoves(board, myPosition);
                break;
            case QUEEN:
                MoveCheck checkQueen = new MoveCheckQueen();
                moves = checkQueen.pieceMoves(board, myPosition);
                break;
            case KING:
                MoveCheck checkKing = new MoveCheckKing();
                moves = checkKing.pieceMoves(board, myPosition);
                break;
        }
        //Return Collection
        return moves;
    }

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;
}
