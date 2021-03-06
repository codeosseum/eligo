package com.codeosseum.eligo.matchmaker.decisiontree;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public final class MatchFunction<P, M> {
    private final Predicate<BucketMatcher<P>> predicate;
    private final Function<PlayerPicker<P>, M> function;

    public static <P, M> MatchFunctionBuilder<P, M> builder() {
        return new MatchFunctionBuilder<>();
    }

    private MatchFunction(final MatchFunctionBuilder<P, M> builder) {
        this.predicate = Optional.ofNullable(builder.predicate).orElse(ctx -> true);
        this.function = Objects.requireNonNull(builder.function);
    }

    Predicate<BucketMatcher<P>> getPredicate() {
        return predicate;
    }

    Function<PlayerPicker<P>, M> getFunction() {
        return function;
    }

    public static final class MatchFunctionBuilder<P, M> {
        private Predicate<BucketMatcher<P>> predicate;
        private Function<PlayerPicker<P>, M> function;

        public MatchFunctionBuilder<P, M> predicate(final Predicate<BucketMatcher<P>> predicate) {
            this.predicate = Objects.requireNonNull(predicate);

            return this;
        }

        public MatchFunctionBuilder<P, M> supplier(final Function<PlayerPicker<P>, M> function) {
            this.function = Objects.requireNonNull(function);

            return this;
        }

        public MatchFunction<P, M> build() {
            return new MatchFunction<>(this);
        }
    }
}
