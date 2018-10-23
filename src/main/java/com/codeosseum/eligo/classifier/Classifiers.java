package com.codeosseum.eligo.classifier;

import java.util.List;

/**
 * This class consists of static methods that can be used to create various {@link Classifier} instances.
 */
public final class Classifiers {

    /**
     * Creates a new {@link Classifier} which uses exact matching to determine whether a player belongs to a class
     * or not. The legal match values are contained within the passed list. The {@code classify} method will respect
     * the ordering of the passed values.
     * @param values the list of expected values
     * @param valueSource the source of actual values
     * @param <P> the player type
     * @param <V> the value type
     * @return a new classifier
     * @throws IllegalArgumentException if the values list is empty or has repeated elements
     * @throws NullPointerException if any of the arguments are empty
     */
    public static <P, V> Classifier<P> exact(final List<V> values, final ValueSource<P, V> valueSource) {
        return ExactMatchingClassifier.fromValues(values, valueSource);
    }

    /**
     * Creates a new classifier which uses intervals to determine classes. A value belongs to an interval if it is
     * greater than or equal to the lower boundary and less than the upper boundary. The classify method
     * will respect the ordering of the elements in this list.
     *
     * The passed list can be empty, which means a single interval.
     * @param boundaries the list of boundaries
     * @param valueSource the value source which will be used for obtaining values
     * @param <P> the player type
     * @param <V> the value type
     * @return a new classifier
     * @throws IllegalArgumentException if the boundaries list is either unsorted or contains repeated elements
     * @throws NullPointerException if any of the arguments is {@code null}
     */
    public static <P, V extends Comparable<V>> Classifier<P> openInterval(final List<V> boundaries, final ValueSource<P, V> valueSource) {
        return OpenIntervalClassifier.fromBoundaries(boundaries, valueSource);
    }

    private Classifiers() {
        // Cannot be constructed.
    }
}
