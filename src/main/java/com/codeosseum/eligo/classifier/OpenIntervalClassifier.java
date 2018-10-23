package com.codeosseum.eligo.classifier;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.nCopies;
import static java.util.Collections.singletonList;

import static com.codeosseum.eligo.util.ListUtils.hasRepeatedElements;
import static com.codeosseum.eligo.util.ListUtils.isSorted;

/**
 * Classifier which labels players based on intervals. The semantics of the classification is described in the documentation
 * of the {@link #fromBoundaries(List, ValueSource)} method.
 * @param <P> the player type
 * @param <V> the value type
 */
final class OpenIntervalClassifier<P, V extends Comparable<V>> extends AbstractClassifier<P> {
    private final ValueSource<P, V> valueSource;

    private final List<V> boundaries;

    /**
     * Creates a new classifier based on the specified boundaries.
     *
     * The list of the boundaries must be sorted and must contain unique elements. The classify method's return value
     * will respect the ordering of the elements in this list.
     *
     * The passed list can be empty, which means a single interval.
     *
     * A value belongs to an interval if it is greater than or equal to the lower boundary and less than the upper
     * boundary.
     * @param boundaries the list of boundaries
     * @param valueSource the value source which will be used for obtaining values
     * @param <P> the player type
     * @param <V> the value type
     * @return a new classifier
     * @throws IllegalArgumentException if the boundaries list is either unsorted or contains repeated elements
     * @throws NullPointerException if any of the arguments is {@code null}
     */
    public static <P, V extends Comparable<V>> OpenIntervalClassifier<P, V> fromBoundaries(final List<V> boundaries, final ValueSource<P, V> valueSource) {
        Objects.requireNonNull(boundaries);
        Objects.requireNonNull(valueSource);

        if (hasRepeatedElements(boundaries)) {
            throw new IllegalArgumentException("Boundaries must contain unique elements!");
        }

        if (!isSorted(boundaries)) {
            throw new IllegalArgumentException("Boundaries must be sorted!");
        }

        return new OpenIntervalClassifier<>(valueSource, boundaries);
    }

    @Override
    public int getClassCount() {
        return boundaries.size() + 1;
    }

    @Override
    protected List<Optional<P>> mapToOptionals(P player) {
        final List<Optional<P>> result;

        if (singleClassClassification()) {
            result = singletonList(Optional.of(player));
        } else {
            result = mapOverIntervals(player);
        }

        return result;
    }

    private OpenIntervalClassifier(final ValueSource<P, V> valueSource, final List<V> boundaries) {
        this.valueSource = valueSource;
        this.boundaries = boundaries;
    }

    private boolean singleClassClassification() {
        return boundaries.isEmpty();
    }

    private List<Optional<P>> mapOverIntervals(P player) {
        final List<Optional<P>> result = boundaries.stream()
                .map(boundary -> classifyInterval(boundary, player))
                .collect(Collectors.toList());

        if (hasNonEmptyClass(result)) {
            fillWithEmptyClasses(result);
        } else {
            addLastClassAsMatch(result, player);
        }

        return result;
    }

    private Optional<P> classifyInterval(V boundary, P player) {
        if (isInInterval(boundary, player)) {
            return Optional.of(player);
        } else {
            return Optional.empty();
        }
    }

    private boolean isInInterval(final V boundary, final P player) {
        final V actualValue = valueSource.get(player);

        return actualValue.compareTo(boundary) < 0;
    }

    private void addLastClassAsMatch(List<Optional<P>> result, P player) {
        result.add(Optional.of(player));
    }

    private boolean hasNonEmptyClass(final List<Optional<P>> optionals) {
        return optionals.stream()
                .anyMatch(Optional::isPresent);
    }

    private void fillWithEmptyClasses(final List<Optional<P>> optionals) {
        final int fillCount = getClassCount() - optionals.size();

        optionals.addAll(nCopies(fillCount, Optional.empty()));
    }
}
