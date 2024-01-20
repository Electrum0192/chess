package chess;

import java.util.*;

public class MoveCheckPawn extends MoveCheck{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        return null;
    }
    @Override
    public Collection<ChessMove> whiteMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        //Move Forward
        ChessPosition oneSpace = new ChessPosition(r+1,c);
        if(board.getPiece(oneSpace) == null && r != 7){
            ChessMove moveForward = new ChessMove(myPosition,oneSpace,null);
            moves.add(moveForward);
        }else if(board.getPiece(oneSpace) == null && r == 7){
            ChessMove promoteRook = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.ROOK);
            ChessMove promoteBishop = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.BISHOP);
            ChessMove promoteKnight = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.KNIGHT);
            ChessMove promoteQueen = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.QUEEN);
            moves.add(promoteRook);
            moves.add(promoteBishop);
            moves.add(promoteKnight);
            moves.add(promoteQueen);
        }
        //Capture Left
        if(c != 1){
            ChessPosition captureSpaceLeft = new ChessPosition(r + 1, c - 1);
            if(board.getPiece(captureSpaceLeft) != null) {
                if (board.getPiece(captureSpaceLeft).getTeamColor() == ChessGame.TeamColor.BLACK && r != 7) {
                    ChessMove captureLeft = new ChessMove(myPosition, captureSpaceLeft, null);
                    moves.add(captureLeft);
                } else if (board.getPiece(captureSpaceLeft).getTeamColor() == ChessGame.TeamColor.BLACK && r == 7) {
                    ChessMove leftPromoteRook = new ChessMove(myPosition, captureSpaceLeft, ChessPiece.PieceType.ROOK);
                    ChessMove leftPromoteBishop = new ChessMove(myPosition, captureSpaceLeft, ChessPiece.PieceType.BISHOP);
                    ChessMove leftPromoteKnight = new ChessMove(myPosition, captureSpaceLeft, ChessPiece.PieceType.KNIGHT);
                    ChessMove leftPromoteQueen = new ChessMove(myPosition, captureSpaceLeft, ChessPiece.PieceType.QUEEN);
                    moves.add(leftPromoteRook);
                    moves.add(leftPromoteBishop);
                    moves.add(leftPromoteKnight);
                    moves.add(leftPromoteQueen);
                }
            }
        }
        //Capture Right
        if(c != 8){
            ChessPosition captureSpaceRight = new ChessPosition(r + 1, c + 1);
            if(board.getPiece(captureSpaceRight) != null) {
                if (board.getPiece(captureSpaceRight).getTeamColor() == ChessGame.TeamColor.BLACK && r != 7) {
                    ChessMove captureRight = new ChessMove(myPosition, captureSpaceRight, null);
                    moves.add(captureRight);
                } else if (board.getPiece(captureSpaceRight).getTeamColor() == ChessGame.TeamColor.BLACK && r == 7) {
                    ChessMove rightPromoteRook = new ChessMove(myPosition, captureSpaceRight, ChessPiece.PieceType.ROOK);
                    ChessMove rightPromoteBishop = new ChessMove(myPosition, captureSpaceRight, ChessPiece.PieceType.BISHOP);
                    ChessMove rightPromoteKnight = new ChessMove(myPosition, captureSpaceRight, ChessPiece.PieceType.KNIGHT);
                    ChessMove rightPromoteQueen = new ChessMove(myPosition, captureSpaceRight, ChessPiece.PieceType.QUEEN);
                    moves.add(rightPromoteRook);
                    moves.add(rightPromoteBishop);
                    moves.add(rightPromoteKnight);
                    moves.add(rightPromoteQueen);
                }
            }
        }
        //Dash
        ChessPosition twoSpace = new ChessPosition(r+2, c);
        if(r == 2 && board.getPiece(oneSpace) == null && board.getPiece(twoSpace) == null){
            ChessMove dash = new ChessMove(myPosition,twoSpace,null);
            moves.add(dash);
        }
        //TODO: En Passant?
        return moves;
    }

    @Override
    public Collection<ChessMove> blackMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        int r = myPosition.getRow();
        int c = myPosition.getColumn();

        //Move Forward
        ChessPosition oneSpace = new ChessPosition(r-1,c);
        if(board.getPiece(oneSpace) == null && r != 2){
            ChessMove moveForward = new ChessMove(myPosition,oneSpace,null);
            moves.add(moveForward);
        }else if(board.getPiece(oneSpace) == null && r == 2){
            ChessMove promoteRook = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.ROOK);
            ChessMove promoteBishop = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.BISHOP);
            ChessMove promoteKnight = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.KNIGHT);
            ChessMove promoteQueen = new ChessMove(myPosition, oneSpace, ChessPiece.PieceType.QUEEN);
            moves.add(promoteRook);
            moves.add(promoteBishop);
            moves.add(promoteKnight);
            moves.add(promoteQueen);
        }
        //Capture Left
        if(c != 1){
            ChessPosition captureSpaceLeft = new ChessPosition(r - 1, c - 1);
            if(board.getPiece(captureSpaceLeft) != null) {
                if (board.getPiece(captureSpaceLeft).getTeamColor() == ChessGame.TeamColor.WHITE && r != 2) {
                    ChessMove captureLeft = new ChessMove(myPosition, captureSpaceLeft, null);
                    moves.add(captureLeft);
                } else if (board.getPiece(captureSpaceLeft).getTeamColor() == ChessGame.TeamColor.WHITE && r == 2) {
                    ChessMove leftPromoteRook = new ChessMove(myPosition, captureSpaceLeft, ChessPiece.PieceType.ROOK);
                    ChessMove leftPromoteBishop = new ChessMove(myPosition, captureSpaceLeft, ChessPiece.PieceType.BISHOP);
                    ChessMove leftPromoteKnight = new ChessMove(myPosition, captureSpaceLeft, ChessPiece.PieceType.KNIGHT);
                    ChessMove leftPromoteQueen = new ChessMove(myPosition, captureSpaceLeft, ChessPiece.PieceType.QUEEN);
                    moves.add(leftPromoteRook);
                    moves.add(leftPromoteBishop);
                    moves.add(leftPromoteKnight);
                    moves.add(leftPromoteQueen);
                }
            }
        }
        //Capture Right
        if(c != 8){
            ChessPosition captureSpaceRight = new ChessPosition(r - 1, c + 1);
            if(board.getPiece(captureSpaceRight) != null) {
                if (board.getPiece(captureSpaceRight).getTeamColor() == ChessGame.TeamColor.WHITE && r != 2) {
                    ChessMove captureRight = new ChessMove(myPosition, captureSpaceRight, null);
                    moves.add(captureRight);
                } else if (board.getPiece(captureSpaceRight).getTeamColor() == ChessGame.TeamColor.WHITE && r == 2) {
                    ChessMove rightPromoteRook = new ChessMove(myPosition, captureSpaceRight, ChessPiece.PieceType.ROOK);
                    ChessMove rightPromoteBishop = new ChessMove(myPosition, captureSpaceRight, ChessPiece.PieceType.BISHOP);
                    ChessMove rightPromoteKnight = new ChessMove(myPosition, captureSpaceRight, ChessPiece.PieceType.KNIGHT);
                    ChessMove rightPromoteQueen = new ChessMove(myPosition, captureSpaceRight, ChessPiece.PieceType.QUEEN);
                    moves.add(rightPromoteRook);
                    moves.add(rightPromoteBishop);
                    moves.add(rightPromoteKnight);
                    moves.add(rightPromoteQueen);
                }
            }
        }
        //Dash
        ChessPosition twoSpace = new ChessPosition(r-2, c);
        if(r == 7 && board.getPiece(oneSpace) == null && board.getPiece(twoSpace) == null){
            ChessMove dash = new ChessMove(myPosition,twoSpace,null);
            moves.add(dash);
        }
        //TODO: En Passant?
        return moves;
    }
}
