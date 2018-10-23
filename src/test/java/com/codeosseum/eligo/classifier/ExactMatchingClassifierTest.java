package com.codeosseum.eligo.classifier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ExactMatchingClassifierTest {
    @Test
    @DisplayName("fromValues should throw NullPointerException if values is null.")
    void fromValuesShouldThrowNullPointerExceptionIfValuesIsNull() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = DummyPlayer::getName;
        final List<String> values = null;

        // Expect
        final Throwable exception = assertThrows(NullPointerException.class, () -> ExactMatchingClassifier.fromValues(values, valueSource));
    }

    @Test
    @DisplayName("fromValues should throw NullPointerException if valueSource is null.")
    void fromValuesShouldThrowNullPointerExceptionIfValueSourceIsNull() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = null;
        final List<String> values = Collections.emptyList();

        // Expect
        final Throwable exception = assertThrows(NullPointerException.class, () -> ExactMatchingClassifier.fromValues(values, valueSource));
    }

    @Test
    @DisplayName("fromValues should throw IllegalArgumentException if values is empty.")
    void fromValuesShouldThrowIllegalArgumentExceptionIfValuesIsEmpty() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = DummyPlayer::getName;
        final List<String> values = Collections.emptyList();

        // Expect
        final Throwable exception = assertThrows(IllegalArgumentException.class, () -> ExactMatchingClassifier.fromValues(values, valueSource));
    }

    @Test
    @DisplayName("fromValues should throw IllegalArgumentException if the elements of values are not unique.")
    void fromValuesShouldThrowIllegalArgumentExceptionIfTheElementsOfValuesAreNotUnique() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = DummyPlayer::getName;
        final List<String> values = Arrays.asList("hello", "hello");

        // Expect
        final Throwable exception = assertThrows(IllegalArgumentException.class, () -> ExactMatchingClassifier.fromValues(values, valueSource));
    }

    private static final class DummyPlayer {
        private String getName() {
            return "name";
        }
    }
}