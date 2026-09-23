package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.team.TeamCreateRequest;
import com.example.football_tournament_api.dto.team.TeamResponse;
import com.example.football_tournament_api.dto.team.TeamUpdateRequest;
import com.example.football_tournament_api.entity.HeadCoach;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.exception.AlreadyExistsException;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.TeamMapper;
import com.example.football_tournament_api.repository.HeadCoachRepository;
import com.example.football_tournament_api.repository.PlayerRepository;
import com.example.football_tournament_api.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private HeadCoachRepository headCoachRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamMapper teamMapper;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    void create_ShouldReturnTeamResponse_WhenRequestIsValid() {
        // Arrange
        TeamCreateRequest request = new TeamCreateRequest("Real Madrid", 1);

        HeadCoach headCoach = new HeadCoach();
        headCoach.setId(1);
        headCoach.setName("Carlo Ancelotti");

        Team mappedTeam = new Team();
        mappedTeam.setName("Real Madrid");

        Team savedTeam = new Team();
        savedTeam.setId(100);
        savedTeam.setName("Real Madrid");
        savedTeam.setHeadCoach(headCoach);

        TeamResponse expectedResponse = new TeamResponse(100, "Real Madrid", 1, "Carlo Ancelotti", 0L, LocalDateTime.now());

        when(teamRepository.existsByName(request.name())).thenReturn(false);
        when(headCoachRepository.findById(request.headCoachId())).thenReturn(Optional.of(headCoach));
        when(teamMapper.toTeam(request)).thenReturn(mappedTeam);
        when(teamRepository.save(mappedTeam)).thenReturn(savedTeam);
        when(teamMapper.toResponse(savedTeam)).thenReturn(expectedResponse);

        // Act
        TeamResponse actualResponse = teamService.create(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        assertEquals(expectedResponse.name(), actualResponse.name());

        verify(teamRepository, times(1)).existsByName(request.name());
        verify(headCoachRepository, times(1)).findById(request.headCoachId());
        verify(teamMapper, times(1)).toTeam(request);
        verify(teamRepository, times(1)).save(mappedTeam);
        verify(teamMapper, times(1)).toResponse(savedTeam);
    }

    @Test
    void create_ShouldThrowAlreadyExistsException_WhenTeamNameExists() {
        // Arrange
        TeamCreateRequest request = new TeamCreateRequest("Real Madrid", 1);

        when(teamRepository.existsByName(request.name())).thenReturn(true);

        // Act & Assert
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> teamService.create(request));

        assertEquals("Team with this name already exists: Real Madrid", exception.getMessage());

        // Fail-fast yoxlaması: Baza xəta verdisə digər servislər işə düşməməlidir
        verify(headCoachRepository, never()).findById(anyInt());
        verify(teamMapper, never()).toTeam(any());
        verify(teamRepository, never()).save(any());
        verify(teamMapper, never()).toResponse(any());
    }

    @Test
    void create_ShouldThrowResourceNotFoundException_WhenHeadCoachDoesNotExist() {
        // Arrange
        TeamCreateRequest request = new TeamCreateRequest("Real Madrid", 99);

        when(teamRepository.existsByName(request.name())).thenReturn(false);
        when(headCoachRepository.findById(request.headCoachId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> teamService.create(request));

        assertEquals("Head Coach Not Found", exception.getMessage());

        verify(teamMapper, never()).toTeam(any());
        verify(teamRepository, never()).save(any());
        verify(teamMapper, never()).toResponse(any());
    }

    @Test
    void update_ShouldUpdateTeamAndChangeCoach_WhenHeadCoachIdIsProvided() {
        // Arrange
        Integer teamId = 1;
        TeamUpdateRequest request = new TeamUpdateRequest("New Team Name", 2);

        Team existingTeam = new Team();
        existingTeam.setId(teamId);
        existingTeam.setName("Old Team Name");

        HeadCoach newHeadCoach = new HeadCoach();
        newHeadCoach.setId(2);
        newHeadCoach.setName("Xabi Alonso");

        TeamResponse expectedResponse = new TeamResponse(teamId, "New Team Name", 2, "Xabi Alonso", 0L, null);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));
        when(headCoachRepository.findById(request.headCoachId())).thenReturn(Optional.of(newHeadCoach));

        doAnswer(invocation -> {
            Team t = invocation.getArgument(1);
            t.setName("New Team Name");
            return null;
        }).when(teamMapper).updateEntityFromRequest(request, existingTeam);

        when(teamRepository.save(existingTeam)).thenReturn(existingTeam);
        when(teamMapper.toResponse(existingTeam)).thenReturn(expectedResponse);

        // Act
        TeamResponse actualResponse = teamService.update(request, teamId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.name(), actualResponse.name());
        assertEquals(newHeadCoach, existingTeam.getHeadCoach());

        verify(teamRepository, times(1)).findById(teamId);
        verify(headCoachRepository, times(1)).findById(request.headCoachId());
        verify(teamMapper, times(1)).updateEntityFromRequest(request, existingTeam);
        verify(teamRepository, times(1)).save(existingTeam);
    }

    @Test
    void update_ShouldUpdateTeamWithoutChangingCoach_WhenHeadCoachIdIsNull() {
        // Arrange
        Integer teamId = 1;
        TeamUpdateRequest request = new TeamUpdateRequest("New Team Name", null);

        Team existingTeam = new Team();
        existingTeam.setId(teamId);

        TeamResponse expectedResponse = new TeamResponse(teamId, "New Team Name", null, null, 0L, null);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));
        when(teamRepository.save(existingTeam)).thenReturn(existingTeam);
        when(teamMapper.toResponse(existingTeam)).thenReturn(expectedResponse);

        // Act
        teamService.update(request, teamId);

        // Assert
        verify(headCoachRepository, never()).findById(anyInt());
        verify(teamMapper, times(1)).updateEntityFromRequest(request, existingTeam);
        verify(teamRepository, times(1)).save(existingTeam);
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenTeamDoesNotExist() {
        // Arrange
        Integer teamId = 99;
        TeamUpdateRequest request = new TeamUpdateRequest("Name", 1);

        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> teamService.update(request, teamId));

        verify(headCoachRepository, never()).findById(anyInt());
        verify(teamMapper, never()).updateEntityFromRequest(any(), any());
        verify(teamRepository, never()).save(any());
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenNewHeadCoachDoesNotExist() {
        // Arrange
        Integer teamId = 1;
        TeamUpdateRequest request = new TeamUpdateRequest("Name", 99);

        Team existingTeam = new Team();
        existingTeam.setId(teamId);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));
        when(headCoachRepository.findById(request.headCoachId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> teamService.update(request, teamId));

        verify(teamMapper, never()).updateEntityFromRequest(any(), any());
        verify(teamRepository, never()).save(any());
    }

    @Test
    void getAll_ShouldReturnListOfTeams() {
        TeamResponse response = new TeamResponse(1, "Real Madrid", 1, "Carlo Ancelotti", 25L, LocalDateTime.now());
        List<TeamResponse> expectedResponses = List.of(response);

        when(teamRepository.findTeamsWithPlayerCount()).thenReturn(expectedResponses);

        List<TeamResponse> actualResponses = teamService.getAll();

        assertNotNull(actualResponses);
        assertEquals(1, actualResponses.size());
        verify(teamRepository, times(1)).findTeamsWithPlayerCount();
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoTeamsExist() {
        when(teamRepository.findTeamsWithPlayerCount()).thenReturn(Collections.emptyList());

        List<TeamResponse> actualResponses = teamService.getAll();

        assertTrue(actualResponses.isEmpty());
        verify(teamRepository, times(1)).findTeamsWithPlayerCount();
    }


    @Test
    void getById_ShouldReturnResponse_WhenTeamExists() {
        Integer teamId = 1;
        TeamResponse expectedResponse = new TeamResponse(teamId, "Real Madrid", 1, "Carlo Ancelotti", 25L, LocalDateTime.now());

        when(teamRepository.findTeamWithPlayerCountById(teamId)).thenReturn(Optional.of(expectedResponse));

        TeamResponse actualResponse = teamService.getById(teamId);

        assertNotNull(actualResponse);
        assertEquals(teamId, actualResponse.id());
        verify(teamRepository, times(1)).findTeamWithPlayerCountById(teamId);
    }

    @Test
    void getById_ShouldThrowResourceNotFoundException_WhenTeamDoesNotExist() {
        Integer teamId = 99;
        when(teamRepository.findTeamWithPlayerCountById(teamId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> teamService.getById(teamId));

        assertEquals("This team not found" + teamId, exception.getMessage());
        verify(teamRepository, times(1)).findTeamWithPlayerCountById(teamId);
    }

    // --- delete() testləri ---
    @Test
    void delete_ShouldDeleteTeamAndUnassignRelations_WhenTeamExistsWithHeadCoach() {
        Integer teamId = 1;
        HeadCoach headCoach = new HeadCoach();
        headCoach.setId(10);

        Team team = new Team();
        team.setId(teamId);
        team.setHeadCoach(headCoach);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        teamService.delete(teamId);

        verify(teamRepository, times(1)).findById(teamId);
        verify(playerRepository, times(1)).unassignPlayersFromTeam(teamId);
        verify(teamRepository, times(1)).unAssignTeamFromHeadCoach(headCoach.getId());
        verify(teamRepository, times(1)).delete(team);
    }

    @Test
    void delete_ShouldDeleteTeamAndUnassignPlayers_WhenTeamExistsWithoutHeadCoach() {
        Integer teamId = 1;
        Team team = new Team();
        team.setId(teamId);
        team.setHeadCoach(null);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        teamService.delete(teamId);

        verify(teamRepository, times(1)).findById(teamId);
        verify(playerRepository, times(1)).unassignPlayersFromTeam(teamId);
        verify(teamRepository, never()).unAssignTeamFromHeadCoach(anyInt());
        verify(teamRepository, times(1)).delete(team);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenTeamDoesNotExist() {
        Integer teamId = 99;
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> teamService.delete(teamId));

        assertEquals("Team not found" + teamId, exception.getMessage());
        verify(playerRepository, never()).unassignPlayersFromTeam(anyInt());
        verify(teamRepository, never()).unAssignTeamFromHeadCoach(anyInt());
        verify(teamRepository, never()).delete(any());
    }

}
