package com.example.football_tournament_api.service;

import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.TournamentType;

public interface FixtureGeneratorStrategy {
    TournamentType getType();

    void generateFixture(Tournament tournament);

    default void generateNextRound(Integer tournamentId, Integer currentRoundNumber) {
        throw new UnsupportedOperationException("This tournament type does not support dynamic round generation.");
    }
}
