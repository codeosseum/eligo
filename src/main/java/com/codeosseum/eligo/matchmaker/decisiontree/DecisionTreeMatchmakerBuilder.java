package com.codeosseum.eligo.matchmaker.decisiontree;

import java.util.ArrayList;
import java.util.List;

import com.codeosseum.eligo.classifier.Classifier;

public class DecisionTreeMatchmakerBuilder<P, M> {
    private final List<Classifier<P>> classifiers;

    private final List<MatchSupplier<P, M>> matchSuppliers;

    public DecisionTreeMatchmakerBuilder() {
        this.classifiers = new ArrayList<>();
        this.matchSuppliers = new ArrayList<>();
    }

    public DecisionTreeMatchmakerBuilder<P, M> classifier(final Classifier<P> classifier) {
        this.classifiers.add(classifier);

        return this;
    }

    public DecisionTreeMatchmakerBuilder<P, M> matchSupplier(final MatchSupplier<P, M> matchSupplier) {
        this.matchSuppliers.add(matchSupplier);

        return this;
    }

    public DecisionTreeMatchmaker<P, M> build() {
        return new DecisionTreeMatchmaker<>(this);
    }

    List<Classifier<P>> getClassifiers() {
        return classifiers;
    }

    List<MatchSupplier<P, M>> getMatchSuppliers() {
        return matchSuppliers;
    }
}
