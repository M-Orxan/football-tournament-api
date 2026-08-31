package com.example.football_tournament_api.service;

import com.example.football_tournament_api.dto.tournament.TournamentStatResponse;
import com.example.football_tournament_api.enums.AggregationType;
import com.example.football_tournament_api.enums.StatType;

import java.util.List;

public interface StatisticsService {
    TournamentStatResponse getTournamentStats(Integer tournamentId, StatType statType,AggregationType aggregationType);
}
