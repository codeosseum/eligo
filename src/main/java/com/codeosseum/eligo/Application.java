package com.codeosseum.eligo;

import com.codeosseum.eligo.classifier.Classifier;
import com.codeosseum.eligo.classifier.Classifiers;
import com.codeosseum.eligo.matchmaker.Matchmaker;
import com.codeosseum.eligo.matchmaker.Matchmakers;
import com.codeosseum.eligo.matchmaker.decisiontree.MatchFunction;

import java.util.Collections;
import java.util.Set;

import static com.codeosseum.eligo.matchmaker.decisiontree.BucketMatcher.hasAtLeast;
import static java.util.Arrays.asList;

public class Application {
    public static void main(String[] args) {
        // Setup
        final MatchFunction<Player, Match> devQAMatch = MatchFunction.<Player, Match>builder()
                .predicate(hasAtLeast(2))
                .supplier(matcher -> new Match(matcher.pickSingle(), matcher.pickSingle()))
                .build();

        final Classifier<Player> scoreClassifier = Classifiers.openInterval(asList(0, 50, 100), Player::getScore);

        final Matchmaker<Player, Match> matchmaker = Matchmakers.<Player, Match>decisionTree()
                .classifier(scoreClassifier)
                .matchFunction(devQAMatch)
                .build();


        // Action
        final Player feri = new Player("Feri", 10);
        final Player anna = new Player("Anna", 60);

        matchmaker.addPlayers(asList(feri, anna));

        // matches.size() is 0
        Set<Match> matches = matchmaker.makeMatch();

        final Player jancsi = new Player("Jancsi", 20);
        matchmaker.addPlayers(Collections.singletonList(jancsi));

        // matches.size() is 1
        matches = matchmaker.makeMatch();

        System.out.println(matches);
    }

    private static final class Match {
        private final Player dev;

        private final Player qa;

        public Match(Player dev, Player qa) {
            this.dev = dev;
            this.qa = qa;
        }
    }

    private static final class Player {
        private final String name;

        private final int score;

        public Player(String name, int score) {
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "name='" + name + '\'' +
                    ", score=" + score +
                    '}';
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }

    private enum Role {
        DEV,
        QA
    }
}
