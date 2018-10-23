package com.codeosseum.eligo.matchmaker.decisiontree;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public final class MatchFunction<P, M> {
    private final Predicate<BucketSelectionContext<P>> predicate;
    private final Function<BucketMatcher<P>, M> function;

    public static <P, M> MatchFunctionBuilder<P, M> builder() {
        return new MatchFunctionBuilder<>();
    }

    private MatchFunction(final MatchFunctionBuilder<P, M> builder) {
        this.predicate = Objects.requireNonNull(builder.predicate);
        this.function = Objects.requireNonNull(builder.function);
    }

    Predicate<BucketSelectionContext<P>> getPredicate() {
        return predicate;
    }

    Function<BucketMatcher<P>, M> getFunction() {
        return function;
    }

    public static final class MatchFunctionBuilder<P, M> {
        private Predicate<BucketSelectionContext<P>> predicate;
        private Function<BucketMatcher<P>, M> function;

        public MatchFunctionBuilder<P, M> predicate(final Predicate<BucketSelectionContext<P>> predicate) {
            this.predicate = Objects.requireNonNull(predicate);

            return this;
        }

        public MatchFunctionBuilder<P, M> supplier(final Function<BucketMatcher<P>, M> function) {
            this.function = Objects.requireNonNull(function);

            return this;
        }

        public MatchFunction<P, M> build() {
            return new MatchFunction<>(this);
        }
    }
}
