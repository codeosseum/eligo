package com.codeosseum.eligo.util;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static com.codeosseum.eligo.util.ListUtils.hasRepeatedElements;
import static com.codeosseum.eligo.util.ListUtils.isSorted;

class ListUtilsTest {
    @ParameterizedTest
    @MethodSource("createCasesForHasRepeatedElements")
    @DisplayName("hasRepeatedElements should work correctly.")
    void hasRepeatedElementsShouldWorkCorrectly(List<String> list, boolean expectedResult) {
        // Expect
        assertEquals(expectedResult, hasRepeatedElements(list));
    }

    @ParameterizedTest
    @MethodSource("createCasesForIsSorted")
    @DisplayName("isSorted should work correctly.")
    void isSortedShouldWorkCorrectly(List<String> list, boolean expectedResult) {
        // Expect
        assertEquals(expectedResult, isSorted(list));
    }

    private static Stream<Arguments> createCasesForHasRepeatedElements() {
        return Stream.of(
            Arguments.of(emptyList(), false),
            Arguments.of(singletonList("single element"), false),
            Arguments.of(asList("element", "element"), true),
            Arguments.of(asList("one", "two", "three"), false)
        );
    }

    private static Stream<Arguments> createCasesForIsSorted() {
        return Stream.of(
            Arguments.of(emptyList(), true),
            Arguments.of(singletonList("single element"), true),
            Arguments.of(asList("we", "are", "unsorted"), false),
            Arguments.of(asList("are", "unsorted", "we"), true)
        );
    }
}