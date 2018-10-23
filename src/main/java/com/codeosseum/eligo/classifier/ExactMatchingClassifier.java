package com.codeosseum.eligo.classifier;

import java.util.*;

public class ExactMatchingClassifier<P, V> extends ValueSourceBasedClassifier<P, V> {
    private final List<V> values;

    /**
     * Creates a new classifier based on the given list of values. The list must contain unique elements. The classify
     * methods return value will respect the ordering of the elements in this list.
     * @param values the list of expected values
     * @param valueSource the source of actual values
     * @param <P> the player type
     * @param <V> the value type
     * @return a new classifier
     * @throws IllegalArgumentException if the values list is empty or has repeated elements
     * @throws NullPointerException if any of the arguments are empty
     */
    public static <P, V> ExactMatchingClassifier<P, V> fromValues(final List<V> values, final ValueSource<P, V> valueSource) {
        Objects.requireNonNull(values);
        Objects.requireNonNull(valueSource);

        if (values.isEmpty()) {
            throw new IllegalArgumentException("Values must not be empty!");
        }

        if (hasRepeatedElements(values)) {
            throw new IllegalArgumentException("Values must contain unique elements!");
        }

        return new ExactMatchingClassifier<>(valueSource, values);
    }

    private static <T> boolean hasRepeatedElements(final List<T> list) {
        final Set<T> set = new HashSet<>(list);

        return set.size() != list.size();
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
