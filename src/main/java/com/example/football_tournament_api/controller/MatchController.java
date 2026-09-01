package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.dto.match.UpdateMatchScoreRequest;
import com.example.football_tournament_api.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @GetMapping("{tournamentId}")
    public ResponseEntity<List<MatchResponse>> getAllByTournamentId(@PathVariable Integer tournamentId){
        List<MatchResponse> response=matchService.getMatchesByTournamentId(tournamentId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("{matchId}/match")
    public ResponseEntity<MatchResponse> getById(@PathVariable Integer matchId){
        MatchResponse response=matchService.getMatchById(matchId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{matchId}/score")
    public ResponseEntity<MatchResponse> updateMatchScore(@PathVariable Integer matchId,@Valid @RequestBody UpdateMatchScoreRequest request){
        MatchResponse response=matchService.updateMatchScore(matchId,request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{tournamentId}/{roundNumber}/simulate")
    public ResponseEntity<List<MatchResponse>> simulateMatchesByRound(@PathVariable Integer tournamentId,@PathVariable int roundNumber){
        List<MatchResponse> responses=matchService.simulateMatchesByRound(tournamentId,roundNumber);
        return ResponseEntity.ok().body(responses);
    }

    @PutMapping("/{tournamentId}/simulate")
    public ResponseEntity<List<MatchResponse>> simulateAllMatchesByTournaments(@PathVariable Integer tournamentId){
        List<MatchResponse> responses=matchService.simulateAllMatchesByTournament(tournamentId);
        return ResponseEntity.ok().body(responses);
    }




}
