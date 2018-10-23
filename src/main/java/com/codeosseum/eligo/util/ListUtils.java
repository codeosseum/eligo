package com.codeosseum.eligo.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ListUtils {
    public static <E> boolean hasRepeatedElements(final List<E> list) {
        final Set<E> set = new HashSet<>(list);

        return set.size() != list.size();
    }

    private ListUtils() {
        // Cannot be constructed.
    }
}
