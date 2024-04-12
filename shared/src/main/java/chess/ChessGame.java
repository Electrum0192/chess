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
            //System.out.println("CURRENT:\n"+currentBoard.toString());
            if(!testBoard.isInCheck(pieceColor)){
                goodMoves.add(i);
            }
        }
        //Check for special moves
        //Castling Left
        if(currentBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING){
            //System.out.println("Board to test:\n"+currentBoard.toString());
            boolean canCastle = true;

            ChessPiece king = currentBoard.getPiece(startPosition);
            ChessPiece leftRook = null;
            if(currentBoard.getPiece(new ChessPosition(startPosition.getRow(),1)) != null) {
                if (currentBoard.getPiece(new ChessPosition(startPosition.getRow(), 1)).getPieceType() == ChessPiece.PieceType.ROOK) {
                    leftRook = currentBoard.getPiece(new ChessPosition(startPosition.getRow(), 1));
                } else {
                    canCastle = false;
                }
            }else canCastle = false;
            //Neither the King nor Rook has moved
            if(canCastle){
                if(leftRook.hasMoved || king.hasMoved){
                    canCastle = false;
                    //System.out.println("One of the pieces has moved.");
                }
            }
            //No pieces in between
            if(canCastle){
                for(int c = 2; c <= 4; c++){
                    if(currentBoard.getPiece(new ChessPosition(startPosition.getRow(),c)) != null){
                        canCastle = false;
                        //System.out.println("A piece is in the way: Column "+c);
                        break;
                    }
                }
            }
            //King is not in check
            if(canCastle){
                if(currentBoard.isInCheck(king.getTeamColor())){
                    canCastle = false;
                    //System.out.println("King is in Check.");
                }
            }
            //Both pieces "safe" after castling
            if(canCastle) {
                //make move
                ChessBoard testBoard = new ChessBoard();
                testBoard.copyBoard(currentBoard);
                ChessMove kingLeft = new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 2), null);
                ChessMove rookRight = new ChessMove(new ChessPosition(startPosition.getRow(), 1), new ChessPosition(startPosition.getRow(), startPosition.getColumn() - 1), null);
                testBoard.movePiece(kingLeft);
                testBoard.movePiece(rookRight);
                //System.out.println("TestBoard after move:\n" + testBoard.toString());
                //Check for Check
                if(testBoard.isInCheck(pieceColor)){
                    canCastle = false;
                    //System.out.println("King is not safe.");
                }
                //Check rook is safe
                if(testBoard.isInDanger(rookRight.getEndPosition())){
                    canCastle = false;
                    //System.out.println("Rook is not safe.");
                }
            }
            //If no problems, add it
            if(canCastle){
                //System.out.println("canCastleLeft = true.");
                ChessPosition kingLeft = new ChessPosition(startPosition.getRow(),startPosition.getColumn()-2);
                //Simpler ChessMove for tests to register, makeMove will adapt to DoubleMove
                ChessMove castleLeftSimple = new ChessMove(startPosition,kingLeft,null);
                goodMoves.add(castleLeftSimple);
            }
        }

        //Castling Right
        if(currentBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING){
            //System.out.println("Board to test:\n"+currentBoard.toString());
            boolean canCastle = true;

            ChessPiece king = currentBoard.getPiece(startPosition);
            ChessPiece rightRook = null;
            if(currentBoard.getPiece(new ChessPosition(startPosition.getRow(),8)) != null) {
                if (currentBoard.getPiece(new ChessPosition(startPosition.getRow(), 8)).getPieceType() == ChessPiece.PieceType.ROOK) {
                    rightRook = currentBoard.getPiece(new ChessPosition(startPosition.getRow(), 8));
                } else {
                    canCastle = false;
                }
            }else canCastle = false;
            //Neither the King nor Rook has moved
            if(canCastle){
                if(rightRook.hasMoved || king.hasMoved){
                    canCastle = false;
                    //System.out.println("One of the pieces has moved.");
                }
            }
            //No pieces in between
            if(canCastle){
                for(int c = 6; c <= 7; c++){
                    if(currentBoard.getPiece(new ChessPosition(startPosition.getRow(),c)) != null){
                        canCastle = false;
                        //System.out.println("A piece is in the way: Column "+c);
                        break;
                    }
                }
            }
            //King is not in check
            if(canCastle){
                if(currentBoard.isInCheck(king.getTeamColor())){
                    canCastle = false;
                    //System.out.println("King is in Check.");
                }
            }
            //Both pieces "safe" after castling
            if(canCastle) {
                //make move
                ChessBoard testBoard = new ChessBoard();
                testBoard.copyBoard(currentBoard);
                ChessMove kingLeft = new ChessMove(startPosition, new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 2), null);
                ChessMove rookRight = new ChessMove(new ChessPosition(startPosition.getRow(), 8), new ChessPosition(startPosition.getRow(), startPosition.getColumn() + 1), null);
                testBoard.movePiece(kingLeft);
                testBoard.movePiece(rookRight);
                //System.out.println("TestBoard after move:\n" + testBoard.toString());
                //Check for Check
                if(testBoard.isInCheck(pieceColor)){
                    canCastle = false;
                    //System.out.println("King is not safe.");
                }
                //Check rook is safe
                if(testBoard.isInDanger(rookRight.getEndPosition())){
                    canCastle = false;
                    //System.out.println("Rook is not safe.");
                }
            }
            //If no problems, add it
            if(canCastle){
                //System.out.println("canCastleRight = true.");
                ChessPosition kingRight = new ChessPosition(startPosition.getRow(),startPosition.getColumn()+2);
                //Simpler ChessMove for tests to register, makeMove will adapt to DoubleMove
                ChessMove castleRightSimple = new ChessMove(startPosition,kingRight,null);
                goodMoves.add(castleRightSimple);
            }
        }

        return goodMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException{
        //Check that the game has not ended
        if(gameOver){
            throw new InvalidMoveException("Move impossible, the game has ended.");
        }

        //Check that there is a piece there
        if(currentBoard.getPiece(move.getStartPosition()) != null){
            //Check whose turn it is
            if(currentBoard.getPiece(move.getStartPosition()).getTeamColor() != activePlayer){
                throw new InvalidMoveException();
            }
        }
        //Check if valid move, if so then execute it
        Collection<ChessMove> valids = validMoves(move.getStartPosition());
        if(!valids.contains(move)){
            throw new InvalidMoveException();
        }else{
            //Set EnPassant flags, if applicable
            currentBoard.getPiece(move.getStartPosition()).switchPassant(move);
            //Check for castling
            boolean castling = false;
            if(currentBoard.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING){
                if(move.getStartPosition().getColumn()-move.getEndPosition().getColumn() == 2){
                    castling = true;
                    //If here it is a left castling move
                    ChessPosition startPosition = move.getStartPosition();
                    ChessPosition kingLeft = new ChessPosition(startPosition.getRow(),startPosition.getColumn()-2);
                    ChessPosition rook = new ChessPosition(startPosition.getRow(),1);
                    ChessPosition rookRight = new ChessPosition(startPosition.getRow(),startPosition.getColumn()-1);
                    DoubleMove castleLeft = new DoubleMove(startPosition,kingLeft,rook,rookRight);
                    currentBoard.movePiece(castleLeft);
                    currentBoard.getPiece(castleLeft.getEndPosition()).hasMoved = true;
                    currentBoard.getPiece(castleLeft.getEndTwo()).hasMoved = true;
                }else if(move.getStartPosition().getColumn()-move.getEndPosition().getColumn() == -2){
                    castling = true;
                    //If here, it is a right castling move
                    ChessPosition startPosition = move.getStartPosition();
                    ChessPosition kingRight = new ChessPosition(startPosition.getRow(),startPosition.getColumn()+2);
                    ChessPosition rook = new ChessPosition(startPosition.getRow(),8);
                    ChessPosition rookLeft = new ChessPosition(startPosition.getRow(),startPosition.getColumn()+1);
                    DoubleMove castleRight = new DoubleMove(startPosition,kingRight,rook,rookLeft);
                    currentBoard.movePiece(castleRight);
                    currentBoard.getPiece(castleRight.getEndPosition()).hasMoved = true;
                    currentBoard.getPiece(castleRight.getEndTwo()).hasMoved = true;
                }
                //If here, piece is King, but move is not castling
            }
            if(!castling) {
                //Move the piece
                currentBoard.movePiece(move);
                //System.out.println(currentBoard.toString());
                //Set hasMoved to true
                currentBoard.getPiece(move.getEndPosition()).hasMoved = true;
            }
            //Update canPassant for all pawns on enemy team
            TeamColor enemy;
            if(activePlayer == TeamColor.WHITE){
                enemy = TeamColor.BLACK;
            }else enemy = TeamColor.WHITE;
            Collection<ChessPosition> enemies = currentBoard.getTeam(enemy);
            for(var i : enemies){
                if(currentBoard.getPiece(i).getPieceType() == ChessPiece.PieceType.PAWN){
                    currentBoard.getPiece(i).switchPassant(move);
                }
            }
            //Change turn
            if(activePlayer == TeamColor.WHITE){
                activePlayer = TeamColor.BLACK;
            }else{activePlayer = TeamColor.WHITE;}
        }
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
        if(!isInCheck(teamColor)) return false;
        //Check valid moves for each piece on the team
        Collection<ChessPosition> places = currentBoard.getTeam(teamColor);
        for(var i : places){
            if(!validMoves(i).isEmpty()){
                return false;
            }
        }
        //If you got here, there should be no valid moves
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)) return false;
        //Check valid moves for each piece on the team
        Collection<ChessPosition> places = currentBoard.getTeam(teamColor);
        for(var i : places){
            if(!validMoves(i).isEmpty()){
                return false;
            }
        }
        //If you got here, there should be no valid moves
        return true;
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

    public void setGameOver(boolean isOver){gameOver = isOver;}

    private ChessGame.TeamColor activePlayer;
    private ChessBoard currentBoard;

    private boolean gameOver;
}
