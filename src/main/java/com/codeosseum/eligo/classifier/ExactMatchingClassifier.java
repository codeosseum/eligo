package com.codeosseum.eligo.classifier;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

import static com.codeosseum.eligo.util.ListUtils.hasRepeatedElements;

/**
 * Classifier that uses exact matching (based on {@code equals}) to perform classification.
 * @param <P> the player type
 * @param <V> the value type
 */
public class ExactMatchingClassifier<P, V> extends AbstractClassifier<P> {
    private final ValueSource<P, V> valueSource;

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

    @Override
    public int getClassCount() {
        return values.size();
    }

    @Override
    protected List<Optional<P>> mapToOptionals(final P player) {
        final V actual = valueSource.get(player);

        return values.stream()
                .map(expected -> expected.equals(actual))
                .map(match -> match ? player : null)
                .map(Optional::ofNullable)
                .collect(toList());
    }

    private ExactMatchingClassifier(final ValueSource<P, V> valueSource, final List<V> values) {
        this.valueSource = valueSource;
        this.values = values;
    }
}
