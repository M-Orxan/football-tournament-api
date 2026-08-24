package com.example.football_tournament_api.service;

public interface TournamentFixtureService {


    void generateFixture(Integer tournamentId);
    void generateNextRound(Integer tournamentId, int currentRoundNumber);

}
