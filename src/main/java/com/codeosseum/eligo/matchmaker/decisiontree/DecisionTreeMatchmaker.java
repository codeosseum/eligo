package com.codeosseum.eligo.matchmaker.decisiontree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.codeosseum.eligo.classifier.Classifier;
import com.codeosseum.eligo.matchmaker.Matchmaker;

class DecisionTreeMatchmaker<P, M> implements Matchmaker<P, M> {
    private final Object lock;

    private final List<Set<P>> buckets;

    private final List<MatchFunction<P, M>> matchFunctions;

    private final Node<P> tree;

    @Override
    public void addPlayer(final P player) {
        synchronized (lock) {
            tree.addPlayer(player);
        }
    }

    @Override
    public void removePlayer(P player) {
        synchronized (lock) {
            buckets.forEach(set -> set.remove(player));
        }
    }

    @Override
    public Set<M> makeMatch() {
        synchronized (lock) {
            return matchFunctions.stream()
                    .map(this::makeMatchUsingFunctionRemovingPlayers)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Set<M> makeMatchAndKeepPlayers() {
        synchronized (lock) {
            return matchFunctions.stream()
                    .map(this::makeMatchUsingFunctionKeepingPlayers)
                    .flatMap(Set::stream)
                    .collect(Collectors.toSet());
        }
    }

    DecisionTreeMatchmaker(final DecisionTreeMatchmakerBuilder<P, M> builder) {
        this.lock = new Object();

        this.buckets = new ArrayList<>();
        this.matchFunctions = builder.getMatchFunctions();

        this.tree = buildTree(builder.getClassifiers());
    }

    private Set<M> makeMatchUsingFunctionRemovingPlayers(final MatchFunction<P, M> matchFunction) {
        return buckets.stream()
                .map(bucket -> makeMatchFromBucketRemovingPlayers(matchFunction, bucket))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toSet());
    }

    private Set<M> makeMatchUsingFunctionKeepingPlayers(final MatchFunction<P, M> matchFunction) {
        return buckets.stream()
                .map(bucket -> makeMatchFromBucketKeepingPlayers(matchFunction, bucket))
                .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                .collect(Collectors.toSet());
    }

    private Optional<M> makeMatchFromBucketRemovingPlayers(final MatchFunction<P, M> function, final Set<P> bucket) {
        final BucketMatcher<P> selectionContext = new BucketMatcher<>(bucket);

        final boolean canMakeMatch = function.getPredicate().test(selectionContext);

        if (canMakeMatch) {
            final PlayerPicker<P> playerPicker = new PlayerPicker<>(bucket);

            final M match = function.getFunction().apply(playerPicker);

            playerPicker.getSelectedPlayers().forEach(this::removePlayer);

            return Optional.of(match);
        }

        return Optional.empty();
    }

    private Optional<M> makeMatchFromBucketKeepingPlayers(final MatchFunction<P, M> function, final Set<P> bucket) {
        final BucketMatcher<P> selectionContext = new BucketMatcher<>(bucket);

        final boolean canMakeMatch = function.getPredicate().test(selectionContext);

        if (canMakeMatch) {
            final PlayerPicker<P> playerPicker = new PlayerPicker<>(bucket);

            return Optional.of(function.getFunction().apply(playerPicker));
        }

        return Optional.empty();
    }

    private Node<P> buildTree(final List<Classifier<P>> classifiers) {
        return makeNode(classifiers, 0);
    }

    private Node<P> makeNode(final List<Classifier<P>> classifiers, final int classifierIndex) {
        if (classifierIndex == classifiers.size()) {
            final BucketNode<P> node = new BucketNode<>();

            this.buckets.add(node.players);

            return node;
        } else {
            final Classifier<P> classifier = classifiers.get(classifierIndex);
            final ClassifierNode<P> node = new ClassifierNode<>(classifier);

            final List<Node<P>> children = IntStream.range(0, classifier.getClassCount())
                    .mapToObj(i -> makeNode(classifiers, classifierIndex + 1))
                    .collect(Collectors.toList());

            node.children.addAll(children);

            return node;
        }
    }

    private static abstract class Node<P> {
        List<Node<P>> children;

        abstract void addPlayer(P player);
    }

    private static final class ClassifierNode<P> extends Node<P> {
        private final Classifier<P> classifier;

        private ClassifierNode(final Classifier<P> classifier) {
            this.children = new ArrayList<>();
            this.classifier = classifier;
        }

        @Override
        void addPlayer(final P player) {
            final List<Optional<P>> classification = classifier.classify(player);

            for (int i = 0; i < classification.size(); ++i) {
                if (classification.get(i).isPresent()) {
                    children.get(i).addPlayer(player);
                    break;
                }
            }
        }
    }

    private static final class BucketNode<P> extends Node<P> {
        private final Set<P> players;

        private BucketNode() {
            this.players = new HashSet<>();
        }

        @Override
        void addPlayer(final P player) {
            this.players.add(player);
        }
    }
}
