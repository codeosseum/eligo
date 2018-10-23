package com.codeosseum.eligo.classifier;

/**
 * Represents a getter function on a specified type.
 * @param <T> the type of the object from which the value is queried
 * @param <V> the type of the supplied value
 */
@FunctionalInterface
public interface ValueSource<T, V> {
    /**
     * Gets a value from the specified object.
     * @param obj the object which is the source of the value
     * @return the queried value
     */
    V get(T obj);
}
