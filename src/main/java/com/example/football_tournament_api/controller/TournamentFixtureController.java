package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.service.TournamentFixtureService;
import com.example.football_tournament_api.service.impl.TournamentFixtureServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tournament-fixture")
@RequiredArgsConstructor
public class TournamentFixtureController {
    private final TournamentFixtureService tournamentFixtureService;



    @PostMapping("/{tournamentId}/fixture")
    public ResponseEntity<List<MatchResponse>> generateFixture(@PathVariable Integer tournamentId){
      List<MatchResponse> response=  tournamentFixtureService.generateFixture(tournamentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{tournamentId}/rounds/{nextRoundNumber}/next")
    public ResponseEntity< List<MatchResponse>> generateNextRound(@PathVariable Integer tournamentId,@PathVariable int nextRoundNumber){
        List<MatchResponse> response=   tournamentFixtureService.generateNextRound(tournamentId,nextRoundNumber);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
