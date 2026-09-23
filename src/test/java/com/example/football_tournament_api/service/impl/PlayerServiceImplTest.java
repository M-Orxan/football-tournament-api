package com.example.football_tournament_api.service.impl;
import com.example.football_tournament_api.dto.player.PlayerCreateRequest;
import com.example.football_tournament_api.dto.player.PlayerResponse;
import com.example.football_tournament_api.dto.player.PlayerUpdateRequest;
import com.example.football_tournament_api.entity.Player;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.exception.AlreadyExistsException;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.PlayerMapper;
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
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerMapper playerMapper;

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Test
    void create_ShouldReturnPlayerResponse_WhenRequestIsValid() {
        // Arrange
        PlayerCreateRequest request = new PlayerCreateRequest("Lionel Messi", 10);

        Team team = new Team();
        team.setId(10);
        team.setName("Inter Miami");

        Player mappedPlayer = new Player();
        mappedPlayer.setName("Lionel Messi");

        Player savedPlayer = new Player();
        savedPlayer.setId(1);
        savedPlayer.setName("Lionel Messi");
        savedPlayer.setTeam(team);
        savedPlayer.setCreatedAt(LocalDateTime.now());
        savedPlayer.setUpdatedAt(LocalDateTime.now());

        PlayerResponse expectedResponse = new PlayerResponse(1, "Lionel Messi",team.getId(),team.getName(),savedPlayer.getCreatedAt(),savedPlayer.getUpdatedAt());

        when(playerRepository.existsByName(request.name())).thenReturn(false);
        when(teamRepository.findById(request.teamId())).thenReturn(Optional.of(team));
        when(playerMapper.toPlayer(request)).thenReturn(mappedPlayer);
        when(playerRepository.save(mappedPlayer)).thenReturn(savedPlayer);
        when(playerMapper.toPlayerResponse(savedPlayer)).thenReturn(expectedResponse);

        // Act
        PlayerResponse actualResponse = playerService.create(request);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());

        // Manual setCreatedAt edildiyi üçün obyektin içinə tarix yazılıb-yazılmadığını yoxlayırıq
        assertNotNull(mappedPlayer.getCreatedAt());

        verify(playerRepository, times(1)).existsByName(request.name());
        verify(teamRepository, times(1)).findById(request.teamId());
        verify(playerMapper, times(1)).toPlayer(request);
        verify(playerRepository, times(1)).save(mappedPlayer);
        verify(playerMapper, times(1)).toPlayerResponse(savedPlayer);
    }

    @Test
    void create_ShouldThrowAlreadyExistsException_WhenPlayerNameExists() {
        // Arrange
        PlayerCreateRequest request = new PlayerCreateRequest("Lionel Messi", 10);

        when(playerRepository.existsByName(request.name())).thenReturn(true);

        // Act & Assert
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> playerService.create(request));

        assertEquals("This name already exists: Lionel Messi", exception.getMessage());

        verify(teamRepository, never()).findById(anyInt());
        verify(playerMapper, never()).toPlayer(any());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowResourceNotFoundException_WhenTeamDoesNotExist() {
        // Arrange
        PlayerCreateRequest request = new PlayerCreateRequest("Lionel Messi", 99);

        when(playerRepository.existsByName(request.name())).thenReturn(false);
        when(teamRepository.findById(request.teamId())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> playerService.create(request));

        assertEquals("This team not found", exception.getMessage());

        verify(playerMapper, never()).toPlayer(any());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void update_ShouldUpdateSuccessfully_WhenTeamIdIsProvided() {
        Integer playerId = 1;
        PlayerUpdateRequest request = new PlayerUpdateRequest("New Name", 10);

        Player existingPlayer = new Player();
        existingPlayer.setId(playerId);
        existingPlayer.setName("Old Name");
        existingPlayer.setUpdatedAt(LocalDateTime.now());
        existingPlayer.setCreatedAt(LocalDateTime.now());

        Team teamProxy = new Team();
        teamProxy.setId(10);
        teamProxy.setName("Chelsea");

        PlayerResponse expectedResponse = new PlayerResponse(1, "New Name", teamProxy.getId(),teamProxy.getName(),existingPlayer.getCreatedAt(),existingPlayer.getUpdatedAt());

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.existsByName(request.name())).thenReturn(false);
        when(teamRepository.existsById(request.teamId())).thenReturn(true);
        when(teamRepository.getReferenceById(request.teamId())).thenReturn(teamProxy);

        doAnswer(inv -> {
            Player p = inv.getArgument(1);
            p.setName("New Name");
            p.setTeam(teamProxy);
            return null;
        }).when(playerMapper).updateEntityFromRequest(request, existingPlayer);

        when(playerMapper.toPlayerResponse(existingPlayer)).thenReturn(expectedResponse);

        PlayerResponse actualResponse = playerService.update(request, playerId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.name(), actualResponse.name());
        verify(teamRepository, times(1)).getReferenceById(request.teamId());
        verify(playerRepository, times(1)).save(existingPlayer);
    }

    @Test
    void update_ShouldUpdateSuccessfully_WhenTeamIdIsNull() {
        Integer playerId = 1;
        String oldName="old name";
        String newName="new name";
        Team team=new Team();
        team.setId(1);
        team.setName("team");
        PlayerUpdateRequest request = new PlayerUpdateRequest(newName, null);

        Player existingPlayer = new Player();
        existingPlayer.setId(playerId);
        existingPlayer.setName(oldName);
        existingPlayer.setTeam(team);
        existingPlayer.setUpdatedAt(LocalDateTime.now());
        existingPlayer.setCreatedAt(LocalDateTime.now());

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.existsByName(request.name())).thenReturn(false);
        doAnswer(inv -> {
            Player p = inv.getArgument(1);
            p.setName(newName);
            return null;
        }).when(playerMapper).updateEntityFromRequest(request, existingPlayer);
        when(playerMapper.toPlayerResponse(existingPlayer)).thenReturn(new PlayerResponse(1, "New Name", null,null,existingPlayer.getCreatedAt(),existingPlayer.getUpdatedAt()));

        playerService.update(request, playerId);

        verify(teamRepository, never()).existsById(any());
        verify(teamRepository, never()).getReferenceById(any());
    }

    @Test
    void update_ShouldThrowAlreadyExistsException_WhenNameIsTakenByAnotherPlayer() {
        Integer playerId = 1;
        PlayerUpdateRequest request = new PlayerUpdateRequest("Taken Name", null);

        Player existingPlayer = new Player();
        existingPlayer.setId(playerId);
        existingPlayer.setName("Old Name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.existsByName(request.name())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> playerService.update(request, playerId));

        verify(playerMapper, never()).updateEntityFromRequest(any(), any());
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenTeamDoesNotExist() {
        Integer playerId = 1;
        PlayerUpdateRequest request = new PlayerUpdateRequest("Name", 99);

        Player existingPlayer = new Player();
        existingPlayer.setId(playerId);
        existingPlayer.setName("Old Name");

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));
        when(playerRepository.existsByName(request.name())).thenReturn(false);
        when(teamRepository.existsById(request.teamId())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> playerService.update(request, playerId));

        verify(teamRepository, never()).getReferenceById(any());
    }


    @Test
    void getById_ShouldReturnResponse_WhenPlayerExists() {
        Integer playerId = 1;
        Team team=new Team();
        team.setId(1);
        team.setName("team");
        Player existingPlayer = new Player();
        existingPlayer.setId(playerId);
        existingPlayer.setName("player");
        existingPlayer.setCreatedAt(LocalDateTime.now());
        existingPlayer.setUpdatedAt(LocalDateTime.now());
        existingPlayer.setTeam(team);

        PlayerResponse expectedResponse = new PlayerResponse(existingPlayer.getId(), existingPlayer.getName(), team.getId(),team.getName(),existingPlayer.getCreatedAt(),existingPlayer.getUpdatedAt());

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(existingPlayer));
        when(playerMapper.toPlayerResponse(existingPlayer)).thenReturn(expectedResponse);

        PlayerResponse actualResponse = playerService.getById(playerId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        verify(playerRepository, times(1)).findById(playerId);
        verify(playerMapper, times(1)).toPlayerResponse(existingPlayer);
    }

    @Test
    void getById_ShouldThrowResourceNotFoundException_WhenPlayerDoesNotExist() {
        Integer playerId = 99;
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> playerService.getById(playerId));

        assertEquals("This player does not exists", exception.getMessage());
        verify(playerMapper, never()).toPlayerResponse(any());
    }

    @Test
    void delete_ShouldDeletePlayer_WhenPlayerExists() {
        Integer playerId = 1;
        Player player = new Player();
        player.setId(playerId);

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        playerService.delete(playerId);

        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, times(1)).delete(player);
    }

    @Test
    void delete_ShouldThrowResourceNotFoundException_WhenPlayerDoesNotExist() {
        Integer playerId = 99;
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> playerService.delete(playerId));

        assertEquals("This player does not exists", exception.getMessage());
        verify(playerRepository, never()).delete(any());
    }


    @Test
    void getAll_ShouldReturnListOfResponses() {
        Team team=new Team();
        team.setId(1);
        team.setName("team");
        Player player = new Player();
        player.setId(1);
        player.setName("player");
        player.setCreatedAt(LocalDateTime.now());
        player.setUpdatedAt(LocalDateTime.now());
        player.setTeam(team);

        PlayerResponse response = new PlayerResponse(player.getId(), player.getName(), team.getId(),team.getName(),player.getCreatedAt(),player.getUpdatedAt());
        List<Player> players = List.of(player);
        List<PlayerResponse> expectedResponses = List.of(response);

        when(playerRepository.findAll()).thenReturn(players);
        when(playerMapper.toResponselist(players)).thenReturn(expectedResponses);

        List<PlayerResponse> actualResponses = playerService.getAll();

        assertNotNull(actualResponses);
        assertEquals(1, actualResponses.size());
        verify(playerRepository, times(1)).findAll();
        verify(playerMapper, times(1)).toResponselist(players);
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoPlayersExist() {
        when(playerRepository.findAll()).thenReturn(Collections.emptyList());
        when(playerMapper.toResponselist(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<PlayerResponse> actualResponses = playerService.getAll();

        assertTrue(actualResponses.isEmpty());
        verify(playerRepository, times(1)).findAll();
    }


}