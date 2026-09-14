package com.example.football_tournament_api.service;


import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.dto.match.UpdateMatchScoreRequest;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.enums.TournamentType;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface MatchService {
    MatchResponse updateMatchScore(Integer matchId, UpdateMatchScoreRequest request);
    public List<MatchResponse> getMatchesByTournamentId(Integer tournamentId);
    public MatchResponse getMatchById(Integer matchId);
    public List<MatchResponse> simulateMatchesByRound(Integer tournamentId, int roundNumber);

    List<MatchResponse> simulateAllMatchesByTournament(Integer tournamentId);

    public List<MatchResponse> getMatchesByTeam(Integer tournamentId,Integer teamId);


}
