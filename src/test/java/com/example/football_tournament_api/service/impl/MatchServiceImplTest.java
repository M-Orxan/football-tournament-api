package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.dto.match.UpdateMatchScoreRequest;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.MatchStatus;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.MatchMapper;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.repository.TeamRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.service.MatchResultProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceImplTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    TeamRepository teamRepository;

    @Mock
    MatchResultProcessorFactory matchResultProcessorFactory;

    @Mock
    MatchResultProcessor matchResultProcessor;

    @Mock
    private MatchMapper matchMapper;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    void getMatchesByTournamentId_ShouldReturnMatches_WhenMatchesExist() {
        // Arrange
        Integer tournamentId = 1;
        Match match = new Match();
        List<Match> matches = List.of(match);
        MatchResponse matchResponse=new MatchResponse(
                1,2,"homeTeam",3,"awayTeam",
                2,3,3,1,tournamentId
        );
        List<MatchResponse> expectedResponses = List.of(matchResponse);

        when(matchRepository.findAllWithTeamsByTournamentId(tournamentId)).thenReturn(matches);
        when(matchMapper.toMatchResponseList(matches)).thenReturn(expectedResponses);

        // Act
        List<MatchResponse> actualResponses = matchService.getMatchesByTournamentId(tournamentId);

        // Assert
        assertNotNull(actualResponses);
        assertEquals(1, actualResponses.size());

        verify(matchRepository, times(1)).findAllWithTeamsByTournamentId(tournamentId);
        verify(matchMapper, times(1)).toMatchResponseList(matches);
        verify(tournamentRepository, never()).existsById(anyInt());
    }

    @Test
    void getMatchesByTournamentId_ShouldThrowResourceNotFoundException_WhenTournamentDoesNotExist() {
        // Arrange
        Integer tournamentId = 99;

        when(matchRepository.findAllWithTeamsByTournamentId(tournamentId)).thenReturn(Collections.emptyList());
        when(tournamentRepository.existsById(tournamentId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> matchService.getMatchesByTournamentId(tournamentId));

        assertEquals("Tournament not found", exception.getMessage());

        verify(matchRepository, times(1)).findAllWithTeamsByTournamentId(tournamentId);
        verify(tournamentRepository, times(1)).existsById(tournamentId);
        verify(matchMapper, never()).toMatchResponseList(any());
    }

    @Test
    void getMatchesByTournamentId_ShouldThrowResourceNotFoundException_WhenTournamentExistsButNoMatches() {
        // Arrange
        Integer tournamentId = 1;

        when(matchRepository.findAllWithTeamsByTournamentId(tournamentId)).thenReturn(Collections.emptyList());
        when(tournamentRepository.existsById(tournamentId)).thenReturn(true);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> matchService.getMatchesByTournamentId(tournamentId));

        assertEquals("Matches have not still been generated for this tournament", exception.getMessage());

        verify(matchRepository, times(1)).findAllWithTeamsByTournamentId(tournamentId);
        verify(tournamentRepository, times(1)).existsById(tournamentId);
        verify(matchMapper, never()).toMatchResponseList(any());
    }

    @Test
    void getMatchById_ShouldReturnResponse_WhenMatchExists() {
        Integer matchId = 1;
        Match match = new Match();
        match.setId(matchId);

        MatchResponse expectedResponse = new MatchResponse(
                matchId, 10, "Real Madrid", 20, "Barcelona", 2, 1, 10, 1, 1
        );

        when(matchRepository.findMatchWithTeamsById(matchId)).thenReturn(Optional.of(match));
        when(matchMapper.toMatchResponse(match)).thenReturn(expectedResponse);

        MatchResponse actualResponse = matchService.getMatchById(matchId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.homeTeam(), actualResponse.homeTeam());

        verify(matchRepository, times(1)).findMatchWithTeamsById(matchId);
        verify(matchMapper, times(1)).toMatchResponse(match);
    }

    @Test
    void getMatchById_ShouldThrowResourceNotFoundException_WhenMatchDoesNotExist() {
        Integer matchId = 99;

        when(matchRepository.findMatchWithTeamsById(matchId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> matchService.getMatchById(matchId));

        assertEquals("Match not found", exception.getMessage());

        verify(matchRepository, times(1)).findMatchWithTeamsById(matchId);
        verify(matchMapper, never()).toMatchResponse(any());
    }

    @Test
    void getMatchesByTeamIdAndTournamentId_ShouldReturnMatches_WhenMatchesExist() {
        // Arrange
        Integer tournamentId = 1;
        Integer teamId = 10;

        Match match = new Match();
        List<Match> matches = List.of(match);

        MatchResponse matchResponse = new MatchResponse(
                100, 10, "Real Madrid", 20, "Barcelona", 2, 1, 10, 1, tournamentId
        );
        List<MatchResponse> expectedResponses = List.of(matchResponse);

        when(matchRepository.findByTournamentIdAndTeamId(tournamentId, teamId)).thenReturn(matches);
        when(matchMapper.toMatchResponseList(matches)).thenReturn(expectedResponses);

        // Act
        List<MatchResponse> actualResponses = matchService.getMatchesByTeamIdAndTournamentId(tournamentId, teamId);

        // Assert
        assertNotNull(actualResponses);
        assertEquals(1, actualResponses.size());

        verify(matchRepository, times(1)).findByTournamentIdAndTeamId(tournamentId, teamId);
        verify(matchMapper, times(1)).toMatchResponseList(matches);


        verify(tournamentRepository, never()).existsById(anyInt());
        verify(teamRepository, never()).existsById(anyInt());
    }

    @Test
    void getMatchesByTeamIdAndTournamentId_ShouldThrowException_WhenTournamentDoesNotExist() {
        // Arrange
        Integer tournamentId = 99;
        Integer teamId = 10;

        when(matchRepository.findByTournamentIdAndTeamId(tournamentId, teamId)).thenReturn(Collections.emptyList());
        when(tournamentRepository.existsById(tournamentId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> matchService.getMatchesByTeamIdAndTournamentId(tournamentId, teamId));

        assertEquals("Tournament not found", exception.getMessage());

        verify(matchRepository, times(1)).findByTournamentIdAndTeamId(tournamentId, teamId);
        verify(tournamentRepository, times(1)).existsById(tournamentId);

        // Turnir tapılmadığı üçün komandanın yoxlanmasına ehtiyac qalmır
        verify(teamRepository, never()).existsById(anyInt());
        verify(matchMapper, never()).toMatchResponseList(any());
    }

    @Test
    void getMatchesByTeamAndTournament_ShouldThrowException_WhenTournamentExistsButTeamDoesNotExist() {
        // Arrange
        Integer tournamentId = 1;
        Integer teamId = 99;

        when(matchRepository.findByTournamentIdAndTeamId(tournamentId, teamId)).thenReturn(Collections.emptyList());
        when(tournamentRepository.existsById(tournamentId)).thenReturn(true);
        when(teamRepository.existsById(teamId)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> matchService.getMatchesByTeamIdAndTournamentId(tournamentId, teamId));

        assertEquals("Team not found", exception.getMessage());

        verify(matchRepository, times(1)).findByTournamentIdAndTeamId(tournamentId, teamId);
        verify(tournamentRepository, times(1)).existsById(tournamentId);
        verify(teamRepository, times(1)).existsById(teamId);
        verify(matchMapper, never()).toMatchResponseList(any());
    }

    @Test
    void getMatchesByTeamAndTournament_ShouldReturnEmptyList_WhenBothExistButNoMatches() {
        // Arrange
        Integer tournamentId = 1;
        Integer teamId = 10;

        when(matchRepository.findByTournamentIdAndTeamId(tournamentId, teamId)).thenReturn(Collections.emptyList());
        when(tournamentRepository.existsById(tournamentId)).thenReturn(true);
        when(teamRepository.existsById(teamId)).thenReturn(true);
        when(matchMapper.toMatchResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Act
        List<MatchResponse> actualResponses = matchService.getMatchesByTeamIdAndTournamentId(tournamentId, teamId);

        // Assert
        assertTrue(actualResponses.isEmpty());

        verify(matchRepository, times(1)).findByTournamentIdAndTeamId(tournamentId, teamId);
        verify(tournamentRepository, times(1)).existsById(tournamentId);
        verify(teamRepository, times(1)).existsById(teamId);
        verify(matchMapper, times(1)).toMatchResponseList(Collections.emptyList());
    }

    @Test
    void updateMatchScore_ShouldUpdateAndProcess_WhenSingleEliminationAndHomeWins() {
        // Arrange
        Integer matchId = 1;
        UpdateMatchScoreRequest request = new UpdateMatchScoreRequest(2, 1);

        Tournament tournament = new Tournament();
        tournament.setId(10);
        tournament.setType(TournamentType.SingleElimination);

        Team homeTeam = new Team();
        homeTeam.setId(100);

        Team awayTeam = new Team();
        awayTeam.setId(200);

        Match match = new Match();
        match.setId(matchId);
        match.setTournament(tournament);
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setRoundNumber(1);

        MatchResponse expectedResponse = new MatchResponse(
                matchId, 100, "Home", 200, "Away", 2, 1, 100, 1, 10
        );

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.existsByTournamentIdAndRoundNumberGreaterThan(10, 1)).thenReturn(false);
        when(matchResultProcessorFactory.getProcessor(TournamentType.SingleElimination)).thenReturn(matchResultProcessor);
        when(matchMapper.toMatchResponse(match)).thenReturn(expectedResponse);

        // Act
        MatchResponse actualResponse = matchService.updateMatchScore(matchId, request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(MatchStatus.Finished, match.getStatus());
        assertEquals(100, match.getWinnerTeamId());

        verify(matchRepository, times(1)).findById(matchId);
        verify(matchMapper, times(1)).updateMatchScore(request, match);
        verify(matchResultProcessor, times(1)).processMatch(match);
    }

    @Test
    void updateMatchScore_ShouldProcessDraw_WhenRoundRobin() {
        // Arrange
        Integer matchId = 1;
        UpdateMatchScoreRequest request = new UpdateMatchScoreRequest(1, 1); // Heç-heçə

        Tournament tournament = new Tournament();
        tournament.setId(10);
        tournament.setType(TournamentType.RoundRobin);

        Match match = new Match();
        match.setId(matchId);
        match.setTournament(tournament);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchResultProcessorFactory.getProcessor(TournamentType.RoundRobin)).thenReturn(matchResultProcessor);
        when(matchMapper.toMatchResponse(match)).thenReturn(new MatchResponse(
                matchId, 100, "Home", 200, "Away", 1, 1, 0, 1, 10
        ));

        // Act
        matchService.updateMatchScore(matchId, request);

        // Assert
        assertEquals(MatchStatus.Finished, match.getStatus());
        verify(matchRepository, never()).existsByTournamentIdAndRoundNumberGreaterThan(anyInt(), anyInt());
        verify(matchResultProcessor, times(1)).processMatch(match);
    }

    @Test
    void updateMatchScore_ShouldThrowIllegalStateException_WhenNextRoundAlreadyGenerated() {
        // Arrange
        Integer matchId = 1;
        UpdateMatchScoreRequest request = new UpdateMatchScoreRequest(2, 1);

        Tournament tournament = new Tournament();
        tournament.setId(10);
        tournament.setType(TournamentType.SingleElimination);

        Match match = new Match();
        match.setId(matchId);
        match.setTournament(tournament);
        match.setRoundNumber(1);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(matchRepository.existsByTournamentIdAndRoundNumberGreaterThan(10, 1)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> matchService.updateMatchScore(matchId, request));

        assertEquals("Next round for this tournament had already been generated. You can't edit for previous round", exception.getMessage());

        verify(matchMapper, never()).updateMatchScore(any(), any());
        verify(matchResultProcessorFactory, never()).getProcessor(any());
    }

    @Test
    void updateMatchScore_ShouldThrowException_WhenScoreIsNull() {
        // Arrange
        Integer matchId = 1;
        UpdateMatchScoreRequest request = new UpdateMatchScoreRequest(null, 1);

        Tournament tournament = new Tournament();
        tournament.setType(TournamentType.SingleElimination);

        Match match = new Match();
        match.setId(matchId);
        match.setTournament(tournament);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));


        doAnswer(inv -> {
            Match m = inv.getArgument(1);
            return null;
        }).when(matchMapper).updateMatchScore(request, match);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> matchService.updateMatchScore(matchId, request));

        assertEquals("Match scores cannot be null", exception.getMessage());
        verify(matchResultProcessorFactory, never()).getProcessor(any());
    }


}