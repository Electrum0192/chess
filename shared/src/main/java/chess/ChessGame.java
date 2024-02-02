package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public ChessGame() {
        activePlayer = TeamColor.WHITE;
        currentBoard = new ChessBoard();
        currentBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return activePlayer;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        activePlayer = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //Is there a piece there?
        if(currentBoard.getPiece(startPosition) == null){
            return null;
        }
        /**
         * Breaks the tests. Grumble Grumble.
        //Is it the correct player's turn?
        if(currentBoard.getPiece(startPosition).getTeamColor() != activePlayer){
            return new HashSet<ChessMove>(); //Empty set, because there are no valid moves
        }
         **/
        //Get moves for that piece
        Collection<ChessMove> allMoves = currentBoard.getPiece(startPosition).pieceMoves(currentBoard, startPosition);
        HashSet<ChessMove> goodMoves = new HashSet<ChessMove>();
        //Check if each move would put you in check
        TeamColor pieceColor = currentBoard.getPiece(startPosition).getTeamColor();
        for(var i : allMoves){
            //Create a copy board and test it
            ChessBoard testBoard = new ChessBoard();
            testBoard.copyBoard(currentBoard);
            testBoard.movePiece(i);
            System.out.println("CURRENT:\n"+currentBoard.toString());
            if(!testBoard.isInCheck(pieceColor)){
                goodMoves.add(i);
            }else{
                System.out.println("FLAG: IN CHECK - " + i.toString());
            }
            System.out.println("Move:\n"+testBoard.toString());

            //TODO add piece-conversion to movePiece
        }

        return goodMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return currentBoard.isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }

    private ChessGame.TeamColor activePlayer;
    private ChessBoard currentBoard;
}
