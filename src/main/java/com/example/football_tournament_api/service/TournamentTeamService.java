package com.example.football_tournament_api.service;

import com.example.football_tournament_api.dto.tournamentTeam.TournamentTeamCreateRequest;
import com.example.football_tournament_api.dto.tournamentTeam.TournamentTeamResponse;
import com.example.football_tournament_api.entity.TournamentTeam;

public interface TournamentTeamService {
    TournamentTeamResponse create(TournamentTeamCreateRequest request);

}
