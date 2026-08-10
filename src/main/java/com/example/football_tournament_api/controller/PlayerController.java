package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.dto.player.PlayerCreateRequest;
import com.example.football_tournament_api.dto.player.PlayerResponse;
import com.example.football_tournament_api.dto.player.PlayerUpdateRequest;
import com.example.football_tournament_api.entity.Player;
import com.example.football_tournament_api.service.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {
    private final PlayerService playerService;

    @PostMapping
    public ResponseEntity<PlayerResponse> create(@Valid @RequestBody  PlayerCreateRequest request){
        PlayerResponse response=playerService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PlayerResponse>> getTeams(){
        return ResponseEntity.ok(playerService.getAll());
    }



    @PutMapping("/{playerId}")
    public ResponseEntity<PlayerResponse> update(@Valid @RequestBody PlayerUpdateRequest request,@PathVariable Integer playerId){
        return ResponseEntity.ok(playerService.update(request,playerId));
    }

    @GetMapping("/{playerId}")
    public ResponseEntity<PlayerResponse> getById(@PathVariable Integer playerId){
        return ResponseEntity.ok(playerService.getById(playerId));
    }

    @DeleteMapping("/{playerId}")
    public ResponseEntity<Void> delete(@PathVariable Integer playerId){
        playerService.delete(playerId);
        return ResponseEntity.noContent().build();
    }


}
