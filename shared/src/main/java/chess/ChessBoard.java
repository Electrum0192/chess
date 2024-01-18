package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        pieces = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int r = position.getRow() - 1;
        int c = position.getColumn() - 1;
        pieces[r][c] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int r = position.getRow() - 1;
        int c = position.getColumn() - 1;

        return pieces[r][c];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //TODO: Add clear function?

        ChessPiece tempPiece;
        ChessPosition tempPos;
        //Black Pawns
        tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for(int a = 1; a <= 8; a++){
            tempPos = new ChessPosition(7,a);
            addPiece(tempPos, tempPiece);
        }
        //White Pawns
        tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        for(int a = 1; a <= 8; a++){
            tempPos = new ChessPosition(2,a);
            addPiece(tempPos, tempPiece);
        }

        //Rooks
        tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        tempPos = new ChessPosition(8,1);
        addPiece(tempPos, tempPiece);
        tempPos = new ChessPosition(8,8);
        addPiece(tempPos, tempPiece);
        tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        tempPos = new ChessPosition(1,1);
        addPiece(tempPos, tempPiece);
        tempPos = new ChessPosition(1,8);
        addPiece(tempPos, tempPiece);

        //Knights
        tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        tempPos = new ChessPosition(8,2);
        addPiece(tempPos, tempPiece);
        tempPos = new ChessPosition(8,7);
        addPiece(tempPos, tempPiece);
        tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        tempPos = new ChessPosition(1,2);
        addPiece(tempPos, tempPiece);
        tempPos = new ChessPosition(1,7);
        addPiece(tempPos, tempPiece);

        //Bishops
        tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        tempPos = new ChessPosition(8,3);
        addPiece(tempPos, tempPiece);
        tempPos = new ChessPosition(8,6);
        addPiece(tempPos, tempPiece);
        tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        tempPos = new ChessPosition(1,3);
        addPiece(tempPos, tempPiece);
        tempPos = new ChessPosition(1,6);
        addPiece(tempPos, tempPiece);

        //Queens
        tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        tempPos = new ChessPosition(8,4);
        addPiece(tempPos, tempPiece);
        tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        tempPos = new ChessPosition(1,4);
        addPiece(tempPos, tempPiece);

        //Kings
        tempPiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        tempPos = new ChessPosition(8,5);
        addPiece(tempPos, tempPiece);
        tempPiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        tempPos = new ChessPosition(1,5);
        addPiece(tempPos, tempPiece);
    }

    private ChessPiece pieces[][];
}
