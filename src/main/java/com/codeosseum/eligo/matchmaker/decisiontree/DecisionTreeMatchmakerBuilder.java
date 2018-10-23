package com.codeosseum.eligo.matchmaker.decisiontree;

import java.util.ArrayList;
import java.util.List;

import com.codeosseum.eligo.classifier.Classifier;
import com.codeosseum.eligo.matchmaker.Matchmaker;

public class DecisionTreeMatchmakerBuilder<P, M> {
    private final List<Classifier<P>> classifiers;

    private final List<MatchFunction<P, M>> matchFunctions;

    public DecisionTreeMatchmakerBuilder() {
        this.classifiers = new ArrayList<>();
        this.matchFunctions = new ArrayList<>();
    }

    public DecisionTreeMatchmakerBuilder<P, M> classifier(final Classifier<P> classifier) {
        this.classifiers.add(classifier);

        return this;
    }

    public DecisionTreeMatchmakerBuilder<P, M> matchSupplier(final MatchFunction<P, M> matchFunction) {
        this.matchFunctions.add(matchFunction);

        return this;
    }

    public Matchmaker<P, M> build() {
        return new DecisionTreeMatchmaker<>(this);
    }

    List<Classifier<P>> getClassifiers() {
        return classifiers;
    }

    List<MatchFunction<P, M>> getMatchFunctions() {
        return matchFunctions;
    }
}
