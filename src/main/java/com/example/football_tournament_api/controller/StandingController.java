package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.dto.standing.StandingResponse;
import com.example.football_tournament_api.service.StandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/standings")
@RequiredArgsConstructor
public class StandingController {
    private final StandingService standingService;

    @GetMapping("/{tournamentId}")
    public ResponseEntity<List<StandingResponse>> getStandings(@PathVariable Integer tournamentId){
        return ResponseEntity.ok(standingService.getAll(tournamentId));

    }
}
