package com.codeosseum.eligo.classifier;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * Classifier that uses exact matching (based on {@code equals}) to perform classification.
 * @param <P> the player type
 * @param <V> the value type
 */
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

    @Override
    public int getClassCount() {
        return values.size();
    }

    @Override
    public List<Optional<P>> classify(final P player) {
        final List<Optional<P>> optionals = mapToOptionals(Objects.requireNonNull(player));

        if (playerIsNotClassified(optionals)) {
            throw new ClassificationException("The specified player does not belong to any of the classes.");
        }

        if (playerIsInMultipleClasses(optionals)) {
            throw new ClassificationException("The specified player belongs to multiple classes.");
        }

        return optionals;
    }

    private static <T> boolean hasRepeatedElements(final List<T> list) {
        final Set<T> set = new HashSet<>(list);

        return set.size() != list.size();
    }

    private ExactMatchingClassifier(final ValueSource<P, V> valueSource, final List<V> values) {
        super(valueSource);

        this.values = values;
    }

    private List<Optional<P>> mapToOptionals(final P player) {
        final V actual = valueSource.get(player);

        return values.stream()
                .map(expected -> expected.equals(actual))
                .map(match -> match ? player : null)
                .map(Optional::ofNullable)
                .collect(toList());
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
