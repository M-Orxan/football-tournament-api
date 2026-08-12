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
import com.example.football_tournament_api.service.TeamService;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {
    private final TeamMapper teamMapper;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final HeadCoachRepository headCoachRepository;

    @Override
    @Transactional
    public TeamResponse create(TeamCreateRequest request) {
        if(teamRepository.existsByName(request.name())){
            throw new AlreadyExistsException("This team exists");
        }


        HeadCoach headCoach=headCoachRepository.findById(request.headCoachId())
                .orElseThrow(()->new ResourceNotFoundException("Head Coach Not Found"));

        if(teamRepository.existsByHeadCoachId(request.headCoachId())){
            throw new AlreadyExistsException("This head coach already assigned to other team");
        }
        Team team=teamMapper.toTeam(request) ;
        team.setCreatedAt(LocalDateTime.now());
        team.setHeadCoach(headCoach);
        Team savedTeam=teamRepository.save(team);
        TeamResponse response=  teamRepository.findTeamWithPlayerCountById(savedTeam.getId()).get();
        //TeamResponse response=teamMapper.toResponse(savedTeam);
        return response;
    }

    @Override
    @Transactional
    public List<TeamResponse> getAll() {

        return teamRepository.findTeamsWithPlayerCount();
    }

    @Override
    @Transactional
    public TeamResponse update(TeamUpdateRequest request, Integer id) {
        Team team=teamRepository.findById(id)
                .orElseThrow(()->new AlreadyExistsException("This team exists"));

        if(teamRepository.existsByName(request.name())&&!team.getName().equals(request.name())){
            throw new AlreadyExistsException("This team exists");
        }


        if(request.headCoachId()!=null){
           HeadCoach headCoach=headCoachRepository.findById(request.headCoachId())
                    .orElseThrow(()->new ResourceNotFoundException("Head coach not found"));

           team.setHeadCoach(headCoach);
        }
        teamMapper.updateEntityFromRequest(request,team);
        teamRepository.save(team);
        TeamResponse response=teamMapper.toResponse(team);
        return response;
    }

    @Override
    @Transactional
    public TeamResponse getById(Integer id) {

          return teamRepository.findTeamWithPlayerCountById(id)
                    .orElseThrow(()->new ResourceNotFoundException("This team not found"+id));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Team team=teamRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Team not found"+id));
        playerRepository.unassignPlayersFromTeam(id);
        if(team.getHeadCoach()!=null){
            teamRepository.unAssignTeamFromHeadCoach(team.getHeadCoach().getId());
        }
       //team.setHeadCoach(null);//bu kod islemedi
        teamRepository.delete(team);
    }
}
