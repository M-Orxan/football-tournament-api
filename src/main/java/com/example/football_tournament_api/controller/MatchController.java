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

    @GetMapping
    public ResponseEntity<List<MatchResponse>> getAllByTournamentId(@RequestParam(required = true) Integer tournamentId){
        List<MatchResponse> response=matchService.getMatchesByTournamentId(tournamentId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("{matchId}")
    public ResponseEntity<MatchResponse> getById(@PathVariable Integer matchId){
        MatchResponse response=matchService.getMatchById(matchId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{matchId}/score")
    public ResponseEntity<MatchResponse> updateMatchScore(@PathVariable Integer matchId,@Valid @RequestBody UpdateMatchScoreRequest request){
        MatchResponse response=matchService.updateMatchScore(matchId,request);
        return ResponseEntity.ok().body(response);
    }


}
