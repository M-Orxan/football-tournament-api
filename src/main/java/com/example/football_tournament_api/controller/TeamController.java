package com.example.football_tournament_api.controller;


import com.example.football_tournament_api.dto.team.TeamCreateRequest;
import com.example.football_tournament_api.dto.team.TeamResponse;
import com.example.football_tournament_api.dto.team.TeamUpdateRequest;
import com.example.football_tournament_api.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getTeams(){
        return ResponseEntity.ok(teamService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeamResponse> getById(@PathVariable Integer id){
        return ResponseEntity.ok(teamService.getById(id));
    }
    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.create(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> update(@Valid @RequestBody(required = false) TeamUpdateRequest request, @PathVariable Integer id){
        return ResponseEntity.ok(teamService.update(request,id));
    }


}
