package com.codeosseum.eligo.classifier;

import java.util.List;
import java.util.Objects;

public final class Classifiers {
    public static <P, V> Classifier<P> exact(final List<V> values, final ValueSource<P, V> valueSource) {
        Objects.requireNonNull(values);
        Objects.requireNonNull(valueSource);

        return ExactMatchingClassifier.fromValues(values, valueSource);
    }

    public static <P, V extends Comparable<V>> Classifier<P> openInterval(final List<V> boundaries, final ValueSource<P, V> valueSource) {
        Objects.requireNonNull(boundaries);
        Objects.requireNonNull(valueSource);

        return OutlierIntervalClassifier.fromBoundaries(boundaries, valueSource);
    }

    private Classifiers() {
        // Cannot be constructed.
    }
}
