package com.codeosseum.eligo.classifier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.codeosseum.eligo.classifier.OutlierIntervalClassifier.fromBoundaries;

class OutlierIntervalClassifierTest {
    @Test
    @DisplayName("fromBoundaries should throw NullPointerException if boundaries is null.")
    void fromValuesShouldThrowNullPointerExceptionIfValuesIsNull() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = DummyPlayer::getName;
        final List<String> boundaries = null;

        // Expect
        final Throwable exception = assertThrows(NullPointerException.class, () -> fromBoundaries(boundaries, valueSource));
    }

    @Test
    @DisplayName("fromBoundaries should throw NullPointerException if valueSource is null.")
    void fromValuesShouldThrowNullPointerExceptionIfValueSourceIsNull() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = null;
        final List<String> boundaries = emptyList();

        // Expect
        final Throwable exception = assertThrows(NullPointerException.class, () -> fromBoundaries(boundaries, valueSource));
    }

    @Test
    @DisplayName("fromValues should throw IllegalArgumentException if boundaries is not sorted.")
    void fromValuesShouldThrowIllegalArgumentExceptionIfValuesIsEmpty() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = DummyPlayer::getName;
        final List<String> boundaries = asList("we", "are", "unsorted");

        // Expect
        final Throwable exception = assertThrows(IllegalArgumentException.class, () -> fromBoundaries(boundaries, valueSource));
    }

    @Test
    @DisplayName("fromValues should throw IllegalArgumentException if the elements of values are not unique.")
    void fromValuesShouldThrowIllegalArgumentExceptionIfTheElementsOfValuesAreNotUnique() {
        // Given
        final ValueSource<DummyPlayer, String> valueSource = DummyPlayer::getName;
        final List<String> boundaries = asList("hello", "hello");

        // Expect
        final Throwable exception = assertThrows(IllegalArgumentException.class, () -> fromBoundaries(boundaries, valueSource));
    }

    @ParameterizedTest
    @MethodSource("createCasesForClassify")
    @DisplayName("classify should work correctly.")
    void classifyShouldWorkCorrectly(final List<String> boundaries, final DummyPlayer player, final List<Optional<DummyPlayer>> expectedClassification) {
        // Given
        final OutlierIntervalClassifier<DummyPlayer, String> classifier = fromBoundaries(boundaries, DummyPlayer::getName);

        // When
        final List<Optional<DummyPlayer>> actualClassification = classifier.classify(player);

        // Then
        assertEquals(expectedClassification, actualClassification);
    }

    private static Stream<Arguments> createCasesForClassify() {
        return Stream.of(
            noBoundaryClassification(),
            singleBoundaryLowerClassification(),
            singleBoundaryUpperClassification(),
            singleBoundaryEdgeCaseClassification(),
            multipleBoundaryMiddleClassification()
        );
    }

    private static Arguments noBoundaryClassification() {
        final DummyPlayer player = DummyPlayer.withName("Dummy");

        return Arguments.of(emptyList(), player, singletonList(Optional.of(player)));
    }

    private static Arguments singleBoundaryLowerClassification() {
        final DummyPlayer player = DummyPlayer.withName("Dummy");
        final List<String> boundaries = singletonList("F");
        final List<Optional<DummyPlayer>> classification = asList(Optional.of(player), Optional.empty());

        return Arguments.of(boundaries, player, classification);
    }

    private static Arguments singleBoundaryUpperClassification() {
        final DummyPlayer player = DummyPlayer.withName("Michael");
        final List<String> boundaries = singletonList("F");
        final List<Optional<DummyPlayer>> classification = asList(Optional.empty(), Optional.of(player));

        return Arguments.of(boundaries, player, classification);
    }

    private static Arguments singleBoundaryEdgeCaseClassification() {
        final DummyPlayer player = DummyPlayer.withName("Dummy");
        final List<String> boundaries = singletonList("Dummy");
        final List<Optional<DummyPlayer>> classification = asList(Optional.empty(), Optional.of(player));

        return Arguments.of(boundaries, player, classification);
    }

    private static Arguments multipleBoundaryMiddleClassification() {
        final DummyPlayer player = DummyPlayer.withName("Dummy");
        final List<String> boundaries = asList("A", "F");
        final List<Optional<DummyPlayer>> classification = asList(Optional.empty(), Optional.of(player), Optional.empty());

        return Arguments.of(boundaries, player, classification);
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