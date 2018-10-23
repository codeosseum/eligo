package com.codeosseum.eligo.matchmaker.decisiontree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public final class PlayerPicker<P> {
    private final Set<P> selectedPlayers;

    private final List<P> availablePlayers;

    public P pickSingle() {
        if (availablePlayers.isEmpty()) {
            throw new IllegalStateException("There are no more players available!");
        }

        final P selected = availablePlayers.remove(availablePlayers.size() - 1);

        selectedPlayers.add(selected);

        return selected;
    }

    public P pickSingle(final Predicate<P> playerPredicate) {
        final P selected = availablePlayers.stream()
                .filter(playerPredicate)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("There is no matching player available!"));

        availablePlayers.remove(selected);

        selectedPlayers.add(selected);

        return selected;
    }

    public List<P> pickMany(final int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than zero!");
        }

        if (count > availablePlayers.size()) {
            throw new IllegalArgumentException("The number of available players is less than the required player count.");
        }

        final List<P> selected = new ArrayList<>(availablePlayers.subList(0, count));

        availablePlayers.removeAll(selected);

        selectedPlayers.addAll(selected);

        return selected;
    }

    public List<P> pickMany(final int count, final Predicate<P> playerPredicate) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be greater than zero!");
        }

        if (count > availablePlayers.size()) {
            throw new IllegalArgumentException("The number of available players is less than the required player count.");
        }

        final List<P> selected = availablePlayers.stream()
                .filter(playerPredicate)
                .limit(count)
                .collect(toList());

        if (selected.size() < count) {
            throw new IllegalArgumentException("The number of matching players is less than the required player count.");
        }

        availablePlayers.removeAll(selected);

        selectedPlayers.addAll(selected);

        return selected;
    }

    public int getAvailablePlayerCount() {
        return availablePlayers.size();
    }

    PlayerPicker(final Set<P> players) {
        this.selectedPlayers = new HashSet<>();
        this.availablePlayers = new ArrayList<>(players);

        Collections.shuffle(this.availablePlayers);
    }

    Set<P> getSelectedPlayers() {
        return selectedPlayers;
    }
}
