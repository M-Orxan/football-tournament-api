package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.dto.tournament.AddTeamToTournamentRequest;
import com.example.football_tournament_api.dto.tournament.TournamentCreateRequest;
import com.example.football_tournament_api.dto.tournament.TournamentResponse;
import com.example.football_tournament_api.service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tournaments")
@RequiredArgsConstructor
@Validated
public class TournamentController {
    private final TournamentService tournamentService;
    @PostMapping
    public ResponseEntity<TournamentResponse> create(@Valid @RequestBody TournamentCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentService.create(request));
    }

    @PostMapping("/add-team")
    public ResponseEntity<Void> create(@Valid @RequestBody AddTeamToTournamentRequest request){
        tournamentService.addTeamToTournament(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
