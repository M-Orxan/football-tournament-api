package com.example.football_tournament_api.service;

import com.example.football_tournament_api.dto.player.PlayerCreateRequest;
import com.example.football_tournament_api.dto.player.PlayerResponse;
import com.example.football_tournament_api.dto.player.PlayerUpdateRequest;

import java.util.List;

public interface GenericService<CreateReq,UpdateReq,Res,Id> {
    Res create(CreateReq request);
    List<Res> getAll();
    Res update(UpdateReq request, Id id);
    Res getById(Id id);
    void delete(Id id);
}
