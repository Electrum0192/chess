package server;

import chess.ChessGame;

/**
 * Request format for JoinGame endpoint
 */
public record JoinRequest(String playerColor, int gameID) {
}
