package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Standing;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.MatchStatus;
import com.example.football_tournament_api.event.TournamentMatchesCreatedEvent;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.repository.StandingRepository;
import com.example.football_tournament_api.repository.TeamRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandingEventListenerTest {

    @Mock
    private StandingRepository standingRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private StandingServiceImpl standingService;

    @Captor
    private ArgumentCaptor<List<Standing>> standingListCaptor;

    @Test
    void onTournamentMatchesCreated_ShouldInitializeStandings_WhenNotExists() {
        Integer tournamentId = 1;
        List<Integer> teamIds = List.of(10, 20);
        TournamentMatchesCreatedEvent event = new TournamentMatchesCreatedEvent(tournamentId, teamIds);

        Tournament tournamentProxy = new Tournament();
        tournamentProxy.setId(tournamentId);

        Team teamProxy1 = new Team();
        teamProxy1.setId(10);

        Team teamProxy2 = new Team();
        teamProxy2.setId(20);

        when(standingRepository.existsByTournamentId(tournamentId)).thenReturn(false);
        when(tournamentRepository.getReferenceById(tournamentId)).thenReturn(tournamentProxy);
        when(teamRepository.getReferenceById(10)).thenReturn(teamProxy1);
        when(teamRepository.getReferenceById(20)).thenReturn(teamProxy2);

        standingService.onTournamentMatchesCreated(event);

        verify(standingRepository, times(1)).existsByTournamentId(tournamentId);
        verify(tournamentRepository, times(1)).getReferenceById(tournamentId);
        verify(teamRepository, times(1)).getReferenceById(10);
        verify(teamRepository, times(1)).getReferenceById(20);

        verify(standingRepository, times(1)).saveAll(standingListCaptor.capture());

        List<Standing> savedStandings = standingListCaptor.getValue();
        assertEquals(2, savedStandings.size());
        assertEquals(tournamentId, savedStandings.get(0).getTournament().getId());
        assertEquals(10, savedStandings.get(0).getTeam().getId());
        assertEquals(20, savedStandings.get(1).getTeam().getId());
    }

    @Test
    void onTournamentMatchesCreated_ShouldThrowIllegalStateException_WhenStandingsExist() {
        Integer tournamentId = 1;
        TournamentMatchesCreatedEvent event = new TournamentMatchesCreatedEvent(tournamentId, List.of(10, 20));

        when(standingRepository.existsByTournamentId(tournamentId)).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> standingService.onTournamentMatchesCreated(event));

        assertEquals("Standing for this tournament is already initialized.", exception.getMessage());

        verify(tournamentRepository, never()).getReferenceById(anyInt());
        verify(teamRepository, never()).getReferenceById(anyInt());
        verify(standingRepository, never()).saveAll(any());
    }

    @Test
    void recalculateTournamentStandings_ShouldResetAndApplyResultsForAllMatches() {
        // Arrange
        Integer tournamentId = 1;

        Team teamHome = new Team(); teamHome.setId(10);
        Team teamAway = new Team(); teamAway.setId(20);

        Standing standingHome = new Standing();
        standingHome.setTeam(teamHome);
        standingHome.setPoints(10); //

        Standing standingAway = new Standing();
        standingAway.setTeam(teamAway);
        standingAway.setPoints(5); //

        List<Standing> standings = List.of(standingHome, standingAway);

        Match match = new Match();
        match.setHomeTeam(teamHome);
        match.setAwayTeam(teamAway);
        match.setHomeTeamScore(2);
        match.setAwayTeamScore(1);
        match.setStatus(MatchStatus.Finished);

        when(standingRepository.findAllByTournamentId(tournamentId)).thenReturn(standings);
        when(matchRepository.findByTournamentIdAndStatus(tournamentId, MatchStatus.Finished)).thenReturn(List.of(match));

        // Act
        standingService.recalculateTournamentStandings(tournamentId);

        // Assert
        assertEquals(3, standingHome.getPoints());
        assertEquals(1, standingHome.getWon());
        assertEquals(2, standingHome.getGoalsFor());
        assertEquals(1, standingHome.getGoalsAgainst());


        assertEquals(0, standingAway.getPoints());
        assertEquals(1, standingAway.getLost());
        assertEquals(1, standingAway.getGoalsFor());
        assertEquals(2, standingAway.getGoalsAgainst());

        verify(standingRepository, times(1)).findAllByTournamentId(tournamentId);
        verify(matchRepository, times(1)).findByTournamentIdAndStatus(tournamentId, MatchStatus.Finished);
    }



}
