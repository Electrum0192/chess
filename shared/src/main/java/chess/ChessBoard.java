package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(pieces);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int a = 7; a >= 0; a--){
            for(int b = 0; b <= 7; b++){
                builder.append('|');
                if(pieces[a][b] == null){
                    builder.append(' ');
                }else if(pieces[a][b].getTeamColor() == ChessGame.TeamColor.BLACK){
                    ChessPiece.PieceType type = pieces[a][b].getPieceType();
                    switch(type){
                        case PAWN:{
                            builder.append('p');
                            break;
                        }
                        case ROOK:{
                            builder.append('r');
                            break;
                        }
                        case BISHOP:{
                            builder.append('b');
                            break;
                        }
                        case KNIGHT:{
                            builder.append('n');
                            break;
                        }
                        case QUEEN:{
                            builder.append('q');
                            break;
                        }
                        case KING:{
                            builder.append('k');
                            break;
                        }
                    }
                }else if(pieces[a][b].getTeamColor() == ChessGame.TeamColor.WHITE){
                    ChessPiece.PieceType type = pieces[a][b].getPieceType();
                    switch(type){
                        case PAWN:{
                            builder.append('P');
                            break;
                        }
                        case ROOK:{
                            builder.append('R');
                            break;
                        }
                        case BISHOP:{
                            builder.append('B');
                            break;
                        }
                        case KNIGHT:{
                            builder.append('N');
                            break;
                        }
                        case QUEEN:{
                            builder.append('Q');
                            break;
                        }
                        case KING:{
                            builder.append('K');
                            break;
                        }
                    }
                }
            }
            builder.append("|\n");
        }
        String output = builder.toString();
        return output;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int r = position.getRow();
        int c = position.getColumn();

        pieces[r-1][c-1] = piece;
    }

    /**
     * Moves a piece on the board. If the piece is capturing, it will override the piece at the destination.
     *
     * @param move The move to make
     */
    public void movePiece(ChessMove move) {
        int sRow = move.getStartPosition().getRow();
        int sColumn = move.getStartPosition().getColumn();
        int eRow = move.getEndPosition().getRow();
        int eColumn = move.getEndPosition().getColumn();

        ChessPiece piece = pieces[sRow-1][sColumn-1];
        //System.out.println(this.toString());
        pieces[eRow-1][eColumn-1] = piece;
        //System.out.println(this.toString());
        pieces[sRow-1][sColumn-1] = null;
        //System.out.println(this.toString());

        //Promotion, if applicable
        if(move.getPromotionPiece() != null){
            pieces[eRow-1][eColumn-1].setPieceType(move.getPromotionPiece());
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int r = position.getRow();
        int c = position.getColumn();

        ChessPiece target = pieces[r-1][c-1];
        return target;
    }

    /**
     * Gets the locations of all pieces of the chosen team
     *
     * @param team The color of pieces you're looking for
     * @return A HashSet of ChessPositions corresponding to the team's pieces
     */
    public Collection<ChessPosition> getTeam(ChessGame.TeamColor team){
        Collection<ChessPosition> locations = new HashSet<ChessPosition>();
        for(int r = 0; r < 8; r++){
            for(int c = 0; c < 8; c++){
                if(pieces[r][c] != null){ //Is there a piece there?
                    if(pieces[r][c].getTeamColor() == team){ //Is it the correct team?
                        locations.add(new ChessPosition(r+1,c+1));
                    }
                }
            }
        }
        return locations;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(ChessGame.TeamColor teamColor) {
        //Get enemy color
        ChessGame.TeamColor enemy;
        if(teamColor == ChessGame.TeamColor.WHITE){enemy = ChessGame.TeamColor.BLACK;}else enemy = ChessGame.TeamColor.WHITE;

        //Search board for enemy pieces and get their moves
        Collection<ChessPosition> places = this.getTeam(enemy);
        for(var i : places){
            Collection<ChessMove> possibleMoves = this.getPiece(i).pieceMoves(this,i);
            //Check each move to see if it could capture the King
            for(var j : possibleMoves){
                if(this.getPiece(j.getEndPosition()) != null){
                    if(this.getPiece(j.getEndPosition()).getPieceType() == ChessPiece.PieceType.KING){
                        //System.out.println("King at "+j.getEndPosition().toString());
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Determines if a given piece is in danger
     *
     * @param piecePlace The position of the piece you are testing
     * @return True if the specified piece is threatened
     */
    public boolean isInDanger(ChessPosition piecePlace) {
        //Get enemy color
        ChessPiece piece = this.getPiece(piecePlace);
        ChessGame.TeamColor teamColor = piece.getTeamColor();
        ChessGame.TeamColor enemy;
        if(teamColor == ChessGame.TeamColor.WHITE){enemy = ChessGame.TeamColor.BLACK;}else enemy = ChessGame.TeamColor.WHITE;

        //Search board for enemy pieces and get their moves
        Collection<ChessPosition> places = this.getTeam(enemy);
        for(var i : places){
            Collection<ChessMove> possibleMoves = this.getPiece(i).pieceMoves(this,i);
            //Check each move to see if it could capture the target piece
            for(var j : possibleMoves){
                if(this.getPiece(j.getEndPosition()) != null){
                    if(j.getEndPosition() == piecePlace){
                        //System.out.println("Threat at "+j.getStartPosition().toString());
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void copyBoard(ChessBoard board){
        for(int r = 1; r <= 8; r++){
            for(int c = 1; c <= 8; c++){
                this.pieces[r-1][c-1] = board.getPiece(new ChessPosition(r,c));
            }
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Pawns
        for(int a = 1; a <= 8; a++){
            ChessPiece wPawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            ChessPiece bPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            ChessPosition wPawnSpace = new ChessPosition(2,a);
            ChessPosition bPawnSpace = new ChessPosition(7,a);
            addPiece(wPawnSpace,wPawn);
            addPiece(bPawnSpace,bPawn);
        }

        ChessPosition tempPos;
        //Rooks
        ChessPiece wRook = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        tempPos = new ChessPosition(1,1);
        addPiece(tempPos,wRook);
        tempPos = new ChessPosition(1,8);
        addPiece(tempPos,wRook);
        ChessPiece bRook = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        tempPos = new ChessPosition(8,1);
        addPiece(tempPos,bRook);
        tempPos = new ChessPosition(8,8);
        addPiece(tempPos,bRook);

        //Knights
        ChessPiece wKnight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        tempPos = new ChessPosition(1,2);
        addPiece(tempPos,wKnight);
        tempPos = new ChessPosition(1,7);
        addPiece(tempPos,wKnight);
        ChessPiece bKnight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        tempPos = new ChessPosition(8,2);
        addPiece(tempPos,bKnight);
        tempPos = new ChessPosition(8,7);
        addPiece(tempPos,bKnight);

        //Bishops
        ChessPiece wBishop = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        tempPos = new ChessPosition(1,3);
        addPiece(tempPos,wBishop);
        tempPos = new ChessPosition(1,6);
        addPiece(tempPos,wBishop);
        ChessPiece bBishop = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        tempPos = new ChessPosition(8,3);
        addPiece(tempPos,bBishop);
        tempPos = new ChessPosition(8,6);
        addPiece(tempPos,bBishop);

        //Queens
        ChessPiece wQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        tempPos = new ChessPosition(1,4);
        addPiece(tempPos,wQueen);
        ChessPiece bQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        tempPos = new ChessPosition(8,4);
        addPiece(tempPos,bQueen);

        //Kings
        ChessPiece wKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        tempPos = new ChessPosition(1,5);
        addPiece(tempPos,wKing);
        ChessPiece bKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        tempPos = new ChessPosition(8,5);
        addPiece(tempPos,bKing);

        System.out.println(toString());

    }

    private ChessPiece[][] pieces;
}
