package chess;

import java.util.Collection;

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
        switch (type){
            case PAWN:
                MoveCheck checkPawn = new MoveCheckPawn();
                checkPawn.pieceMoves(board, myPosition);
                break;
            case BISHOP:
                MoveCheck checkBishop = new MoveCheckBishop();
                checkBishop.pieceMoves(board, myPosition);
                break;
            case ROOK:
                MoveCheck checkRook = new MoveCheckRook();
                checkRook.pieceMoves(board, myPosition);
                break;
            case KNIGHT:
                MoveCheck checkKnight = new MoveCheckKnight();
                checkKnight.pieceMoves(board, myPosition);
                break;
            case QUEEN:
                MoveCheck checkQueen = new MoveCheckQueen();
                checkQueen.pieceMoves(board, myPosition);
                break;
            case KING:
                MoveCheck checkKing = new MoveCheckKing();
                checkKing.pieceMoves(board, myPosition);
                break;
        }
        //Calculate possible moves based on type
        //If Pawn, check for promotion (each is a separate move)
        //Return Collection
    }

    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;
}
