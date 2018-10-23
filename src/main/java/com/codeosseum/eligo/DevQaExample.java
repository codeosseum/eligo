package com.codeosseum.eligo;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.codeosseum.eligo.classifier.Classifier;
import com.codeosseum.eligo.classifier.Classifiers;
import com.codeosseum.eligo.matchmaker.Matchmaker;
import com.codeosseum.eligo.matchmaker.Matchmakers;
import com.codeosseum.eligo.matchmaker.decisiontree.MatchFunction;

import static java.util.Arrays.asList;

import static com.codeosseum.eligo.matchmaker.decisiontree.BucketMatcher.hasAtLeast;

public class DevQaExample {
    public static void main(String[] args) {
        // Setup Matchmaker
        final Predicate<Player> isDev = player -> player.role.equals(Role.DEV);
        final Predicate<Player> isQA = player -> player.role.equals(Role.QA);

        final MatchFunction<Player, Match> devQaMatch = MatchFunction.<Player, Match>builder()
                .predicate(hasAtLeast(Match.TEAMS, isDev).and(hasAtLeast(Match.TEAMS, isQA)))
                .supplier(picker -> {
                    final List<Team> teams = IntStream.range(0, Match.TEAMS)
                            .mapToObj(i -> new Team(picker.pickSingle(isDev), picker.pickSingle(isQA)))
                            .collect(Collectors.toList());

                    return new Match(teams);
                })
                .build();

        final Classifier<Player> rankClassifier = Classifiers.openInterval(asList(0, 10, 20, 30, 40, 50), Player::getRank);

        final Matchmaker<Player, Match> matchmaker = Matchmakers.<Player, Match>decisionTree()
                .classifier(rankClassifier)
                .matchFunction(devQaMatch)
                .build();

        // Setup Players
        final List<Player> players = asList(
                new Player("Feri", Role.DEV, 5),
                new Player("Juli", Role.DEV, 6),
                new Player("Annuska", Role.QA, 7),
                new Player("Laci", Role.QA, 4)
        );

        // Action
        players.forEach(matchmaker::addPlayer);

        final Set<Match> matches = matchmaker.makeMatch();

        System.out.println(matches);
    }

    private static final class Match {
        private static final int TEAMS = 2;

        private final List<Team> teams;

        private Match(List<Team> teams) {
            this.teams = teams;
        }

        @Override
        public String toString() {
            return "Match{" +
                    "teams=" + teams +
                    '}';
        }
    }

    private static final class Team {
        private final Player dev;

        private final Player qa;

        private Team(Player dev, Player qa) {
            this.dev = dev;
            this.qa = qa;
        }

        @Override
        public String toString() {
            return "Team{" +
                    "dev=" + dev +
                    ", qa=" + qa +
                    '}';
        }
    }

    private static final class Player {
        private final String name;

        private final Role role;

        private final int rank;

        public Player(String name, Role role, int rank) {
            this.name = name;
            this.role = role;
            this.rank = rank;
        }

        public String getName() {
            return name;
        }

        public Role getRole() {
            return role;
        }

        public int getRank() {
            return rank;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "name='" + name + '\'' +
                    ", role=" + role +
                    ", rank=" + rank +
                    '}';
        }
    }

    private enum Role {
        DEV, QA
    }
}
