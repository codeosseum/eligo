package com.codeosseum.eligo.classifier;

import java.util.List;
import java.util.Optional;

public class ExactMatchingClassifier<P, V> extends ValueSourceBasedClassifier<P, V> {
    private final List<V> values;

    public static <P, V> ExactMatchingClassifier<P, V> fromValues(final List<V> values, final ValueSource<P, V> valueSource) {
        return new ExactMatchingClassifier<>(valueSource, values);
    }

    protected ExactMatchingClassifier(final ValueSource<P, V> valueSource, final List<V> values) {
        super(valueSource);
        this.values = values;
    }

    @Override
    public int getClassCount() {
        return 0;
    }

    @Override
    public List<Optional<P>> classify(P player) {
        return null;
    }
}
