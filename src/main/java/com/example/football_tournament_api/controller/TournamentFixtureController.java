package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.service.TournamentFixtureService;
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
    private final TournamentFixtureService tournamentFixtureService;

//    @PostMapping("/roundRobin/{tournamentId}")
//    public ResponseEntity<Void> generateRoundRobinMatches(@PathVariable Integer tournamentId){
//        tournamentFixtureService.generateRoundRobinMatches(tournamentId);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
//    @PostMapping("/singleElimination/{tournamentId}")
//    public ResponseEntity<Void> generateSingleEliminationMatches(@PathVariable Integer tournamentId){
//        tournamentFixtureService.generateSingleEliminationMatches(tournamentId);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @PostMapping("/{tournamentId}/fixture")
    public ResponseEntity<Void> generateFixture(@PathVariable Integer tournamentId){
        tournamentFixtureService.generateFixture(tournamentId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{tournamentId}/rounds/{currentRoundNumber}/next")
    public ResponseEntity<Void> generateNextRound(@PathVariable Integer tournamentId,@PathVariable int currentRoundNumber){
        tournamentFixtureService.generateNextRound(tournamentId,currentRoundNumber);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
