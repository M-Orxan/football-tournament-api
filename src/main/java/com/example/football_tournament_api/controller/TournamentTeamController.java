package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.dto.tournamentTeam.TournamentTeamCreateRequest;
import com.example.football_tournament_api.dto.tournamentTeam.TournamentTeamResponse;
import com.example.football_tournament_api.service.TournamentTeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tournament-teams")
@RequiredArgsConstructor
public class TournamentTeamController {
    private final TournamentTeamService tournamentTeamService;
    @PostMapping
    public ResponseEntity<TournamentTeamResponse> create(@Valid @RequestBody TournamentTeamCreateRequest request){
       TournamentTeamResponse response= tournamentTeamService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
