package com.example.football_tournament_api.service;


import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.dto.match.UpdateMatchScoreRequest;
import com.example.football_tournament_api.entity.Team;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface MatchService {
    MatchResponse updateMatchScore(Integer matchId,UpdateMatchScoreRequest request);
    public List<MatchResponse> getMatchesByTournamentId(Integer tournamentId);
    public MatchResponse getMatchById(Integer matchId);
}
