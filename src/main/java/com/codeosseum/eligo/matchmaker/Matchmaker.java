package com.codeosseum.eligo.matchmaker;

import java.util.Set;

/**
 * Interface for services that can create matches from a given set of players.
 * @param <P> Type parameter representing a player.
 * @param <M> Type parameter representing a match.
 */
public interface Matchmaker<P, M> {
    /**
     * Adds a specified player to the matchmaking lobby. If the player is already present, then
     * does nothing.
     * @param player the player to add
     */
    void addPlayer(P player);

    /**
     * Removes the specified player from the matchmaking lobby. If the player is not present, then
     * does nothing.
     * @param player the player to remove
     */
    void removePlayer(P player);

    /**
     * Creates new matches from the previously added players. If no matches can be made, then an empty set is returned.
     * Players contained within the returned matches are automatically removed from the lobby.
     * @return a set of matches
     */
    Set<M> makeMatch();
}
