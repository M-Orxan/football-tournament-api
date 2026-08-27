package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.dto.statistics.TeamStatResponse;
import com.example.football_tournament_api.enums.StatType;
import com.example.football_tournament_api.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/stats")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping("{tournamentId}")
    public ResponseEntity<List<TeamStatResponse>> getTeamByDesiredStatType(@PathVariable Integer tournamentId,
                                                                           @RequestParam StatType statType

    ){
        List<TeamStatResponse> response= statisticsService.getTeamStatByDesiredStatType(tournamentId,statType);
        return ResponseEntity.ok().body(response);
    }
}
