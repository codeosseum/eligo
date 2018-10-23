package com.codeosseum.eligo.matchmaker;

import com.codeosseum.eligo.matchmaker.decisiontree.DecisionTreeMatchmakerBuilder;

public final class Matchmakers {
    public static <P, M> DecisionTreeMatchmakerBuilder<P, M> decisionTree() {
        return new DecisionTreeMatchmakerBuilder<>();
    }

    private Matchmakers() {
        // Cannot be constructed.
    }
}
