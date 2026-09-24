package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.tournament.AddTeamToTournamentRequest;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.entity.TournamentTeam;
import com.example.football_tournament_api.exception.AlreadyExistsException;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.TournamentTeamMapper;
import com.example.football_tournament_api.repository.TeamRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.repository.TournamentTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TournamentTeamServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TournamentTeamRepository tournamentTeamRepository;

    @Mock
    private TournamentTeamMapper tournamentTeamMapper;

    @InjectMocks
    private TournamentServiceImpl tournamentService;

    @Test
    void addTeamToTournament_ShouldAddSuccessfully_WhenRequestIsValid() {
        // Arrange
        AddTeamToTournamentRequest request = new AddTeamToTournamentRequest(1, 10);

        Tournament tournament = new Tournament();
        tournament.setId(1);

        Team team = new Team();
        team.setId(10);

        TournamentTeam tournamentTeam = new TournamentTeam();

        when(tournamentRepository.findById(request.tournamentId())).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(request.teamId())).thenReturn(Optional.of(team));
        when(tournamentTeamRepository.existsByTournamentIdAndTeamId(request.tournamentId(), request.teamId())).thenReturn(false);
        when(tournamentTeamMapper.toTournamentTeam(tournament, team)).thenReturn(tournamentTeam);

        // Act
        tournamentService.addTeamToTournament(request);

        // Assert
        verify(tournamentRepository, times(1)).findById(request.tournamentId());
        verify(teamRepository, times(1)).findById(request.teamId());
        verify(tournamentTeamRepository, times(1)).existsByTournamentIdAndTeamId(request.tournamentId(), request.teamId());
        verify(tournamentTeamMapper, times(1)).toTournamentTeam(tournament, team);
        verify(tournamentTeamRepository, times(1)).save(tournamentTeam);
    }

    @Test
    void addTeamToTournament_ShouldThrowResourceNotFoundException_WhenTournamentDoesNotExist() {
        AddTeamToTournamentRequest request = new AddTeamToTournamentRequest(1, 10);

        when(tournamentRepository.findById(request.tournamentId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tournamentService.addTeamToTournament(request));

        assertEquals("Tournament not found", exception.getMessage());

        verify(teamRepository, never()).findById(anyInt());
        verify(tournamentTeamRepository, never()).existsByTournamentIdAndTeamId(anyInt(), anyInt());
        verify(tournamentTeamRepository, never()).save(any());
    }

    @Test
    void addTeamToTournament_ShouldThrowResourceNotFoundException_WhenTeamDoesNotExist() {
        AddTeamToTournamentRequest request = new AddTeamToTournamentRequest(1, 10);
        Tournament tournament = new Tournament();

        when(tournamentRepository.findById(request.tournamentId())).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(request.teamId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tournamentService.addTeamToTournament(request));

        assertEquals("Team not found: 10", exception.getMessage());

        verify(tournamentTeamRepository, never()).existsByTournamentIdAndTeamId(anyInt(), anyInt());
        verify(tournamentTeamRepository, never()).save(any());
    }

    @Test
    void addTeamToTournament_ShouldThrowAlreadyExistsException_WhenTeamAlreadyRegistered() {
        AddTeamToTournamentRequest request = new AddTeamToTournamentRequest(1, 10);
        Tournament tournament = new Tournament();
        Team team = new Team();

        when(tournamentRepository.findById(request.tournamentId())).thenReturn(Optional.of(tournament));
        when(teamRepository.findById(request.teamId())).thenReturn(Optional.of(team));
        when(tournamentTeamRepository.existsByTournamentIdAndTeamId(request.tournamentId(), request.teamId())).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> tournamentService.addTeamToTournament(request));

        assertEquals("Team is already registered to this tournament: 10", exception.getMessage());

        verify(tournamentTeamMapper, never()).toTournamentTeam(any(), any());
        verify(tournamentTeamRepository, never()).save(any());
    }
}
