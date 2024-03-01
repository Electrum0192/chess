package server;

import chess.ChessGame;

/**
 * Request format for JoinGame endpoint
 */
public record JoinRequest(ChessGame.TeamColor playerColor, int gameID) {
}
