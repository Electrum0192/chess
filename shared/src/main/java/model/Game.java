package model;

/**
 * A modified form of GameData, used by the ListGames endpoint
 * @param gameID
 * @param whiteUsername
 * @param blackUsername
 * @param gameName
 */
public record Game(int gameID, String whiteUsername, String blackUsername, String gameName) {
}
