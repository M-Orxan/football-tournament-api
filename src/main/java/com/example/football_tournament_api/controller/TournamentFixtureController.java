package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.service.impl.TournamentFixtureServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tournament-fixture")
@RequiredArgsConstructor
public class TournamentFixtureController {
    private final TournamentFixtureServiceImpl tournamentFixtureServiceImpl;

    @PostMapping("/{tournamentId}")
    public ResponseEntity<Void> generateMatches(@PathVariable Integer tournamentId){
        tournamentFixtureServiceImpl.generateMatches(tournamentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
