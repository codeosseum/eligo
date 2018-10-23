package com.codeosseum.eligo.classifier;

import java.util.List;
import java.util.Optional;

/**
 * Interface for player classifiers.
 * @param <P> the player type
 */
public interface Classifier<P> {
    /**
     * Returns the number of classes.
     * @return the number of classes
     */
    int getClassCount();

    /**
     * Determines the class the specified player belongs to. The length of the returned list is
     * equal to the value returned by {@link #getClassCount()}. Each place in the returned list corresponds to a
     * class. If the classified player does not belong to a class, then an empty optional is placed on the
     * appropriate index. As a consequence, there is only a single non-empty optional in the returned list.
     * @param player the player to be classified
     * @return the result of the classification
     * @throws NullPointerException if the player is {@code null}
     */
    List<Optional<P>> classify(P player);
}
