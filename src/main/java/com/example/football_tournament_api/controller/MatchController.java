package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @GetMapping("/{tournamentId}")
    public ResponseEntity<List<MatchResponse>> getAll(@PathVariable Integer tournamentId){
        List<MatchResponse> response=matchService.getAllMatches(tournamentId);
        return ResponseEntity.ok().body(response);
    }

}
