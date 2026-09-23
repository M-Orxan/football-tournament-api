package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.tournament.AddTeamToTournamentRequest;
import com.example.football_tournament_api.dto.tournament.TournamentCreateRequest;
import com.example.football_tournament_api.dto.tournament.TournamentResponse;
import com.example.football_tournament_api.dto.tournament.TournamentUpdateRequest;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.entity.TournamentTeam;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.TournamentMapper;
import com.example.football_tournament_api.mapper.TournamentTeamMapper;
import com.example.football_tournament_api.repository.TeamRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.repository.TournamentTeamRepository;
import com.example.football_tournament_api.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TournamentMapper tournamentMapper;
    private final TeamRepository teamRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final TournamentTeamMapper tournamentTeamMapper;

    @Override
    public TournamentResponse create(TournamentCreateRequest request) {
        if(tournamentRepository.existsByName(request.name())){
            throw new ResourceNotFoundException("This tournament already exists: "+request.name());
        }

        //if(request.type().)
        Tournament tournament=tournamentMapper.toEntity(request);
        Tournament savedTournament=tournamentRepository.save(tournament);

        return tournamentMapper.toResponse(savedTournament);
    }

    @Override
    public List<TournamentResponse> getAll() {
        return List.of();
    }

    @Override
    public TournamentResponse update(TournamentUpdateRequest request, Integer integer) {
        return null;
    }

    @Override
    public TournamentResponse getById(Integer integer) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void addTeamToTournament(AddTeamToTournamentRequest request) {

        Tournament tournament = tournamentRepository.findById(request.tournamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found: "+request.teamId()));
        boolean isRegistered = tournamentTeamRepository.existsByTournamentIdAndTeamId(request.tournamentId(), request.teamId());
        if (isRegistered) {
            throw new IllegalStateException("Team is already registered to this tournament: "+request.teamId());
        }

        TournamentTeam tournamentTeam = tournamentTeamMapper.toTournamentTeam(tournament, team);
        TournamentTeam savedTournamentTeam = tournamentTeamRepository.save(tournamentTeam);
    }
}
