package com.example.football_tournament_api.service;

import com.example.football_tournament_api.dto.statistics.TeamStatResponse;
import com.example.football_tournament_api.enums.StatType;

import java.math.BigDecimal;
import java.util.List;

public interface StatisticsService {
    List<TeamStatResponse> getTeamStatByDesiredStatType(Integer tournamentId, StatType statType);
}
