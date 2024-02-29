package server;

import model.Game;

import java.util.Collection;

/**
 * A collection of Games, used by the ListGames endpoint
 * @param games
 */

public record GameList(Collection<Game> games) {
}
