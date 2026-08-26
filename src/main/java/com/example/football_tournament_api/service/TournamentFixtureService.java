package com.example.football_tournament_api.service;

import com.example.football_tournament_api.dto.match.MatchResponse;

import java.util.List;

public interface TournamentFixtureService {


    List<MatchResponse> generateFixture(Integer tournamentId);
    List<MatchResponse> generateNextRound(Integer tournamentId, int currentRoundNumber);

}
