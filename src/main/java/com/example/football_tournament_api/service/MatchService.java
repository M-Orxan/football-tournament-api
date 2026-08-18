package com.example.football_tournament_api.service;


import com.example.football_tournament_api.dto.match.MatchResponse;

import java.util.List;

public interface MatchService {
    void updateMatchScore(Integer matchId,int homeTeamScore, int awayTeamScore);
    public List<MatchResponse> getAllMatches(Integer tournamentId);
}
