package com.codeosseum.eligo.matchmaker.decisiontree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.codeosseum.eligo.classifier.Classifier;
import com.codeosseum.eligo.matchmaker.Matchmaker;

class DecisionTreeMatchmaker<P, M> implements Matchmaker<P, M> {
    private final Set<BucketNode<P>> bucketNodes;

    private final Node<P> tree;

    DecisionTreeMatchmaker(final DecisionTreeMatchmakerBuilder<P, M> builder) {
        this.bucketNodes = new HashSet<>();

        this.tree = buildTree(builder.getClassifiers());
    }

    @Override
    public void addPlayer(P player) {
        tree.addPlayer(player);
    }

    @Override
    public void removePlayer(P player) {
        bucketNodes.stream()
                .map(node -> node.players)
                .forEach(set -> set.remove(player));
    }

    @Override
    public Set<M> makeMatch() {
        return null;
    }

    private Node<P> buildTree(final List<Classifier<P>> classifiers) {
        return makeNode(classifiers, 0);
    }

    private Node<P> makeNode(final List<Classifier<P>> classifiers, final int classifierIndex) {
        if (classifierIndex == classifiers.size()) {
            final BucketNode<P> node = new BucketNode<>();

            this.bucketNodes.add(node);

            return node;
        } else {
            final Classifier<P> classifier = classifiers.get(classifierIndex);
            final ClassifierNode<P> node = new ClassifierNode<>(classifier);

            node.children.addAll(Collections.nCopies(classifier.getClassCount(), makeNode(classifiers, classifierIndex + 1)));

            return node;
        }
    }

    private static abstract class Node<P> {
        protected List<Node<P>> children;

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
