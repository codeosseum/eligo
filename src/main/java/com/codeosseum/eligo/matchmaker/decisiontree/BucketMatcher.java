package com.codeosseum.eligo.matchmaker.decisiontree;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public final class BucketMatcher<P> {
    private final Set<P> players;

    public static <P> Predicate<BucketMatcher<P>> all(final Predicate<P> playerPredicate) {
        Objects.requireNonNull(playerPredicate);

        return ctx -> ctx.players.stream().allMatch(playerPredicate);
    }

    public static <P> Predicate<BucketMatcher<P>> hasAtLeast(final int count) {
        return hasAtLeast(count, p -> true);
    }

    public static <P> Predicate<BucketMatcher<P>> hasAtLeast(final int count, final Predicate<P> playerPredicate) {
        Objects.requireNonNull(playerPredicate);

        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than zero!");
        }

        return ctx -> ctx.players.stream()
                .filter(playerPredicate)
                .count() >= count;
    }

    BucketMatcher(final Set<P> players) {
        this.players = players;
    }
}
