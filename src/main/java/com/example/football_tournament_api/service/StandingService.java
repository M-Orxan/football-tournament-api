package com.example.football_tournament_api.service;

import com.example.football_tournament_api.dto.standing.StandingResponse;
import com.example.football_tournament_api.event.MatchFinishedEvent;
import com.example.football_tournament_api.event.TournamentMatchesCreatedEvent;

import java.util.List;

public interface StandingService {
    void onMatchFinished(MatchFinishedEvent event);
    void onTournamentMatchesCreated(TournamentMatchesCreatedEvent event);
    List<StandingResponse> getAll(Integer tournamentId);
    void recalculateFromZer(Integer tournamentId);
}
