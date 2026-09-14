package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.tournamentTeam.TournamentTeamCreateRequest;
import com.example.football_tournament_api.dto.tournamentTeam.TournamentTeamResponse;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.entity.TournamentTeam;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.TournamentTeamMapper;
import com.example.football_tournament_api.repository.TeamRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.repository.TournamentTeamRepository;
import com.example.football_tournament_api.service.TournamentTeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TournamentTeamServiceImpl implements TournamentTeamService {
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final TournamentTeamMapper tournamentTeamMapper;

    @Override
    public TournamentTeamResponse create(TournamentTeamCreateRequest request) {
        Tournament tournament = tournamentRepository.findById(request.tournamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));
        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found"));

        boolean isRegistered = tournamentTeamRepository.existsByTournamentIdAndTeamId(request.tournamentId(), request.teamId());
        if (isRegistered) {
            throw new IllegalStateException("Team is already registered to this tournament");
        }

        TournamentTeam tournamentTeam = tournamentTeamMapper.toTournamentTeam(tournament, team);
        TournamentTeam savedTournamentTeam = tournamentTeamRepository.save(tournamentTeam);

        return tournamentTeamMapper.toTournamentTeamResponse(savedTournamentTeam);
    }


}
