package com.codeosseum.eligo.classifier;

import java.util.List;
import java.util.Optional;

/**
 * Classifier which uses a {@link ValueSource} as the basis of the classification.
 * @param <P> the player type
 * @param <V> the value type
 */
public abstract class ValueSourceBasedClassifier<P, V> implements Classifier<P> {
    protected final ValueSource<P, V> valueSource;

    protected ValueSourceBasedClassifier(final ValueSource<P, V> valueSource) {
        this.valueSource = valueSource;
    }

    public abstract int getClassCount();

    public abstract List<Optional<P>> classify(P player);
}
