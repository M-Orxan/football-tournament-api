package com.example.football_tournament_api.service;

import com.example.football_tournament_api.dto.team.TeamCreateRequest;
import com.example.football_tournament_api.dto.team.TeamResponse;
import com.example.football_tournament_api.dto.team.TeamUpdateRequest;

public interface TeamService extends GenericService<TeamCreateRequest, TeamUpdateRequest, TeamResponse,Integer>{
}
