package com.example.football_tournament_api.service;


import com.example.football_tournament_api.dto.player.PlayerCreateRequest;
import com.example.football_tournament_api.dto.player.PlayerResponse;
import com.example.football_tournament_api.dto.player.PlayerUpdateRequest;

import java.util.List;

public interface PlayerService {
    PlayerResponse create(PlayerCreateRequest request);
    List<PlayerResponse> getAll();
    PlayerResponse update(PlayerUpdateRequest request, Integer playerId);
    PlayerResponse getById(Integer playerId);
    void delete(Integer playerId);

}
