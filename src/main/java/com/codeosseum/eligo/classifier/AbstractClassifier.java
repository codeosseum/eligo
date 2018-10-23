package com.codeosseum.eligo.classifier;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Abstract base class implementing common logic for classification.
 * @param <P> the player type
 */
public abstract class AbstractClassifier<P> implements Classifier<P> {
    @Override
    public final List<Optional<P>> classify(P player) {
        final List<Optional<P>> optionals = mapToOptionals(Objects.requireNonNull(player));

        if (invalidClassCount(optionals)) {
            throw new IllegalStateException("Invalid number of classes returned!");
        }

        if (playerIsNotClassified(optionals)) {
            throw new ClassificationException("The specified player does not belong to any of the classes.");
        }

        if (playerIsInMultipleClasses(optionals)) {
            throw new ClassificationException("The specified player belongs to multiple classes.");
        }

        return optionals;
    }

    /**
     * Maps the specified player to a list of optionals. An empty optional should be placed in the list
     * if the player does not belong to a class, otherwise a non-empty one.
     * @param player the player to classify
     * @return a list of classification optionals
     */
    protected abstract List<Optional<P>> mapToOptionals(P player);

    private boolean invalidClassCount(final List<Optional<P>> optionals) {
        return optionals.size() != this.getClassCount();
    }

    private boolean playerIsNotClassified(final List<Optional<P>> optionals) {
        return optionals.stream()
                .noneMatch(Optional::isPresent);
    }

    private boolean playerIsInMultipleClasses(final List<Optional<P>> optionals) {
        return optionals.stream()
                .filter(Optional::isPresent)
                .count() > 1;
    }
}
