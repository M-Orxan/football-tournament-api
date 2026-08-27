package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.statistics.TeamStatResponse;
import com.example.football_tournament_api.entity.Standing;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.StatType;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.StatisticsMapper;
import com.example.football_tournament_api.repository.StandingRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StandingRepository standingRepository;
    private final StatisticsMapper statisticsMapper;
    private final TournamentRepository tournamentRepository;

    @Override
    public List<TeamStatResponse> getTeamStatByDesiredStatType(Integer tournamentId, StatType statType) {
        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResourceNotFoundException("Tournament not found");
        }


        List<Standing> standings = standingRepository.findAllByTournamentIdOrderByPointsDescGoalDifferenceDescGoalsForDesc(tournamentId);

        int maxValue = standings.stream()
                .mapToInt(statType::extractValue).max().orElse(0);

        if (maxValue == 0) {
            throw new ResourceNotFoundException("No records found for stat type: " + statType);
        }

        List<Standing> topStandings = standings.stream()
                .filter(s -> statType.extractValue(s) == maxValue).toList();

        return statisticsMapper.toResponseList(topStandings, statType);
    }
}
