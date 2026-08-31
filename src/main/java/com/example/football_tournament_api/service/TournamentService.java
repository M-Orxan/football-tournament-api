package com.example.football_tournament_api.service;

import com.example.football_tournament_api.dto.tournament.AddTeamToTournamentRequest;
import com.example.football_tournament_api.dto.tournament.TournamentCreateRequest;
import com.example.football_tournament_api.dto.tournament.TournamentResponse;
import com.example.football_tournament_api.dto.tournament.TournamentUpdateRequest;

public interface TournamentService extends GenericService<TournamentCreateRequest, TournamentUpdateRequest, TournamentResponse,Integer> {

     void addTeamToTournament( AddTeamToTournamentRequest request);


}
