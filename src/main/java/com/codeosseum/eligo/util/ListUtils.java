package com.codeosseum.eligo.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ListUtils {
    public static <E> boolean hasRepeatedElements(final List<E> list) {
        final Set<E> set = new HashSet<>(list);

        return set.size() != list.size();
    }

    public static <E extends Comparable<E>> boolean isSorted(final List<E> list) {
        for (int i = 0; i < list.size() - 1; ++i) {
            if (list.get(i).compareTo(list.get(i + 1)) > 0) {
                return false;
            }
        }

        return true;
    }

    private ListUtils() {
        // Cannot be constructed.
    }
}
