package com.example.football_tournament_api.service.impl;


import com.example.football_tournament_api.dto.tournament.TournamentStatResponse;
import com.example.football_tournament_api.entity.Standing;
import com.example.football_tournament_api.enums.AggregationType;
import com.example.football_tournament_api.enums.StatType;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.StatisticsMapper;
import com.example.football_tournament_api.repository.StandingRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.service.StatisticsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    private final StandingRepository standingRepository;
    private final StatisticsMapper statisticsMapper;
    private final TournamentRepository tournamentRepository;

    @Override
    @Transactional
    public TournamentStatResponse getTournamentStats(Integer tournamentId, StatType statType, AggregationType aggregationType) {

        if (!tournamentRepository.existsById(tournamentId)) {
            throw new ResourceNotFoundException("Tournament not found");
        }

        if (!tournamentRepository.isGivenType(tournamentId, TournamentType.RoundRobin)) {
            throw new ResourceNotFoundException("Stats are available only for round robin tournaments");
        }

        List<Standing> standings = standingRepository.findAllByTournamentIdOrderByPointsDescGoalDifferenceDescGoalsForDesc(tournamentId);


        return switch (aggregationType) {
            case MAX -> calculateMaxStats(tournamentId, standings, statType);
            case MIN -> calculateMinStats(tournamentId, standings, statType);
        };
    }


    private TournamentStatResponse calculateMaxStats(Integer tournamentId, List<Standing> standings, StatType statType) {
        double maxValue = standings.stream()
                .mapToInt(statType::extractValue).max().orElse(0);

        List<Standing> topStandings = standings.stream()
                .filter(s -> statType.extractValue(s) == maxValue).toList();

        return statisticsMapper.toTournamentStatResponse(tournamentId, statType, AggregationType.MAX, maxValue, topStandings);
    }

    private TournamentStatResponse calculateMinStats(Integer tournamentId, List<Standing> standings, StatType statType) {
        double minValue = standings.stream()
                .mapToInt(statType::extractValue).min().orElse(0);

        List<Standing> bottomStandings = standings.stream()
                .filter(s -> statType.extractValue(s) == minValue).toList();

        return statisticsMapper.toTournamentStatResponse(tournamentId, statType, AggregationType.MIN, minValue, bottomStandings);
    }


}

