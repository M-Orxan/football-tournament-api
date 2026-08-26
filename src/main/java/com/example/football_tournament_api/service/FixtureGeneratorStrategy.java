package com.example.football_tournament_api.service;

import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.TournamentType;

import java.util.List;

public interface FixtureGeneratorStrategy {
    TournamentType getType();

    List<Match> generateFixture(Tournament tournament);

    default List<Match> generateNextRound(Integer tournamentId, Integer currentRoundNumber) {
        throw new UnsupportedOperationException("This tournament type does not support dynamic round generation.");
    }
}
