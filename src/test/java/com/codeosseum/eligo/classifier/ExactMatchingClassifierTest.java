package com.codeosseum.eligo.classifier;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.codeosseum.eligo.classifier.ExactMatchingClassifier.fromValues;

class ExactMatchingClassifierTest {
    private static final String DUMMY_NAME = "dummy";

    @Test
    @DisplayName("fromValues should throw NullPointerException if values is null.")
    void fromValuesShouldThrowNullPointerExceptionIfValuesIsNull() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = DummyPlayer::getName;
        final List<String> values = null;

        // Expect
        final Throwable exception = assertThrows(NullPointerException.class, () -> fromValues(values, valueSource));
    }

    @Test
    @DisplayName("fromValues should throw NullPointerException if valueSource is null.")
    void fromValuesShouldThrowNullPointerExceptionIfValueSourceIsNull() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = null;
        final List<String> values = Collections.emptyList();

        // Expect
        final Throwable exception = assertThrows(NullPointerException.class, () -> fromValues(values, valueSource));
    }

    @Test
    @DisplayName("fromValues should throw IllegalArgumentException if values is empty.")
    void fromValuesShouldThrowIllegalArgumentExceptionIfValuesIsEmpty() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = DummyPlayer::getName;
        final List<String> values = Collections.emptyList();

        // Expect
        final Throwable exception = assertThrows(IllegalArgumentException.class, () -> fromValues(values, valueSource));
    }

    @Test
    @DisplayName("fromValues should throw IllegalArgumentException if the elements of values are not unique.")
    void fromValuesShouldThrowIllegalArgumentExceptionIfTheElementsOfValuesAreNotUnique() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = DummyPlayer::getName;
        final List<String> values = asList("hello", "hello");

        // Expect
        final Throwable exception = assertThrows(IllegalArgumentException.class, () -> fromValues(values, valueSource));
    }

    @ParameterizedTest
    @MethodSource("createValuesForGetClassCount")
    @DisplayName("getClassCount should return the number of elements in values.")
    void getClassCountShouldReturnTheNumberOfElementsInValues(List<String> values) {
        // Given
        final ExactMatchingClassifier<DummyPlayer, String> classifier = fromValues(values, DummyPlayer::getName);
        final int expectedCount = values.size();

        // When
        final int actualCount = classifier.getClassCount();

        // Then
        assertEquals(expectedCount, actualCount);
    }

    @Test
    @DisplayName("classify should throw NullPointerException if the player is null.")
    void classifyShouldThrowNullPointerExceptionIfThePlayerIsNull() {
        // Given
        final DummyPlayer player = null;
        final ExactMatchingClassifier<DummyPlayer, String> classifier = fromValues(singletonList("name"), DummyPlayer::getName);

        // Expect
        final Throwable exception = assertThrows(NullPointerException.class, () -> classifier.classify(player));
    }

    @Test
    @DisplayName("classify should throw ClassificationException if the player cannot be classified.")
    void classifyShouldThrowClassificationExceptionIfThePlayerCannotBeClassified() {
        // Given
        final DummyPlayer player = new DummyPlayer(DUMMY_NAME);
        final ExactMatchingClassifier<DummyPlayer, String> classifier = fromValues(singletonList("Bob"), DummyPlayer::getName);

        // Expect
        final Throwable exception = assertThrows(ClassificationException.class, () -> classifier.classify(player));
    }

    @ParameterizedTest
    @MethodSource("createClassificationParameters")
    @DisplayName("classify should correctly classify the player.")
    void classifyShouldCorrectlyClassifyThePlayer(List<String> values, DummyPlayer player, List<Optional<DummyPlayer>> expectedClassification) {
        // Given
        final ExactMatchingClassifier<DummyPlayer, String> classifier = fromValues(values, DummyPlayer::getName);

        // When
        final List<Optional<DummyPlayer>> actualClassification = classifier.classify(player);

        // Then
        assertEquals(expectedClassification, actualClassification);
    }

    private static Stream<Arguments> createClassificationParameters() {
        return Stream.of(
            singleClassClassification(),
            multipleClassClassification()
        );
    }

    private static Stream<Arguments> createValuesForGetClassCount() {
        return Stream.of(
            Arguments.of(singletonList("Bob")),
            Arguments.of(asList("hello", "there")),
            Arguments.of(asList("three", "element", "list"))
        );
    }

    private static Arguments singleClassClassification() {
        final DummyPlayer player = DummyPlayer.withName(DUMMY_NAME);

        return Arguments.of(singletonList(DUMMY_NAME), player, singletonList(Optional.of(player)));
    }

    private static Arguments multipleClassClassification() {
        final DummyPlayer player = DummyPlayer.withName(DUMMY_NAME);
        final List<String> values = asList("something", DUMMY_NAME, "other");
        final List<Optional<DummyPlayer>> classification =
                asList(Optional.empty(), Optional.of(player), Optional.empty());

        return Arguments.of(values, player, classification);
    }

    private static final class DummyPlayer {
        private final String name;

        private static DummyPlayer withName(final String name) {
            return new DummyPlayer(name);
        }

        private DummyPlayer(String name) {
            this.name = name;
        }

        private String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DummyPlayer that = (DummyPlayer) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}