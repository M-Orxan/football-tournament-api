package com.example.football_tournament_api.service;

import com.example.football_tournament_api.dto.match.UpdateMatchScoreRequest;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.enums.TournamentType;

public interface MatchResultProcessor {

    TournamentType getType();
    void processMatch(Match match);
}
