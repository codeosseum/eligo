package com.codeosseum.eligo;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.codeosseum.eligo.matchmaker.Matchmaker;
import com.codeosseum.eligo.matchmaker.Matchmakers;
import com.codeosseum.eligo.matchmaker.decisiontree.MatchFunction;

import static java.util.Arrays.asList;

import static com.codeosseum.eligo.matchmaker.decisiontree.BucketMatcher.hasAtLeast;

public final class SimpleTwoTeamExample {
    public static void main(String[] args) {
        // Setup Matchmaker
        final MatchFunction<Player, Match> simpleMatch = MatchFunction.<Player, Match>builder()
                .predicate(hasAtLeast(Match.TEAMS * Match.PLAYERS_PER_TEAM))
                .supplier(picker -> {
                    final List<List<Player>> teams = IntStream.range(0, Match.TEAMS)
                            .mapToObj(i -> picker.pickMany(Match.PLAYERS_PER_TEAM))
                            .collect(Collectors.toList());

                    return new Match(teams);
                })
                .build();

        final Matchmaker<Player, Match> matchmaker = Matchmakers.<Player, Match>decisionTree()
                .matchFunction(simpleMatch)
                .build();

        // Setup Players
        final List<Player> players = asList(
                Player.withName("Feri"),
                Player.withName("Juli"),
                Player.withName("Annuska"),
                Player.withName("Laci")
        );

        // Action
        players.forEach(matchmaker::addPlayer);

        final Set<Match> matches = matchmaker.makeMatch();

        System.out.println(matches);
    }

    private static final class Match {
        private static final int TEAMS = 2;
        private static final int PLAYERS_PER_TEAM = 2;

        private final List<List<Player>> teams;

        private Match(List<List<Player>> teams) {
            this.teams = teams;
        }

        @Override
        public String toString() {
            return "Match{" +
                    "teams=" + teams +
                    '}';
        }
    }

    private static final class Player {
        private final String name;

        private static Player withName(final String name) {
            return new Player(name);
        }

        private Player(String name) {
            this.name = name;
        }

        private String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
