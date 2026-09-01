package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.dto.tournament.TournamentStatResponse;
import com.example.football_tournament_api.enums.AggregationType;
import com.example.football_tournament_api.enums.StatType;
import com.example.football_tournament_api.service.StatisticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("{tournamentId}")
    public ResponseEntity<TournamentStatResponse> getTeamByDesiredStatType(@Valid @PathVariable Integer tournamentId,
                                                                           @Valid @RequestParam StatType statType,
                                                                           @Valid @RequestParam AggregationType aggregationType

    ) {
        TournamentStatResponse response = statisticsService.getTournamentStats(tournamentId, statType, aggregationType);
        return ResponseEntity.ok().body(response);
    }
}
