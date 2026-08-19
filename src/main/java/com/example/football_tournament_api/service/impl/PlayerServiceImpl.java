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
import com.example.football_tournament_api.service.PlayerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public PlayerResponse create(PlayerCreateRequest request) {

        if (playerRepository.existsByName(request.name())) {
            throw new AlreadyExistsException("This name already exists: " + request.name());
        }

        Team team=teamRepository.findById(request.teamId())
                .orElseThrow(()->new ResourceNotFoundException("This team not found"));


        Player player = playerMapper.toPlayer(request);
        player.setCreatedAt(LocalDateTime.now());
        player.setTeam(team);
        Player savedPlayer = playerRepository.save(player);
        PlayerResponse response = playerMapper.toPlayerResponse(savedPlayer);
        return response;
    }

    @Override
    @Transactional
    public List<PlayerResponse> getAll() {
        List<Player> players = playerRepository.findAll();
        List<PlayerResponse> responseList = playerMapper.toResponselist(players);
        return responseList;
    }

    @Override
    @Transactional
    public PlayerResponse update(PlayerUpdateRequest request, Integer playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("This player does not exists"));


        if (playerRepository.existsByName(request.name()) && !request.name().equals(player.getName())) {
            throw new AlreadyExistsException("This name already exists: " + request.name());
        }

        if (request.teamId() != null) {
            if (!teamRepository.existsById(request.teamId())) {
                throw new ResourceNotFoundException("Team not found");
            }
        }

        if (request.teamId() != null) {
            Team teamProxy = teamRepository.getReferenceById(request.teamId());
            player.setTeam(teamProxy);
        }

        playerMapper.updateEntityFromRequest(request, player);
        player.setUpdatedAt(LocalDateTime.now());
        playerRepository.save(player);
        PlayerResponse response = playerMapper.toPlayerResponse(player);
        return response;
    }

    @Override
    @Transactional
    public PlayerResponse getById(Integer playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("This player does not exists"));
        PlayerResponse response = playerMapper.toPlayerResponse(player);

        return response;
    }

    @Override
    @Transactional
    public void delete(Integer playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("This player does not exists"));
        playerRepository.delete(player);
    }
}
