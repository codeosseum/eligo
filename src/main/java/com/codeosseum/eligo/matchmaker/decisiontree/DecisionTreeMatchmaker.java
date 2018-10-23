package com.codeosseum.eligo.matchmaker.decisiontree;

import java.util.Set;

import com.codeosseum.eligo.matchmaker.Matchmaker;

class DecisionTreeMatchmaker<P, M> implements Matchmaker<P, M> {
    DecisionTreeMatchmaker(final DecisionTreeMatchmakerBuilder<P, M> builder) {
    }

    @Override
    public void addPlayer(P player) {

    }

    @Override
    public void removePlayer(P player) {

    }

    @Override
    public Set<M> makeMatch() {
        return null;
    }
}
