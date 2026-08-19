package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.head_coach.HeadCoachCreateRequest;
import com.example.football_tournament_api.dto.head_coach.HeadCoachResponse;
import com.example.football_tournament_api.dto.head_coach.HeadCoachUpdateRequest;
import com.example.football_tournament_api.entity.HeadCoach;
import com.example.football_tournament_api.exception.AlreadyExistsException;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.HeadCoachMapper;
import com.example.football_tournament_api.repository.HeadCoachRepository;
import com.example.football_tournament_api.repository.TeamRepository;
import com.example.football_tournament_api.service.HeadCoachService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeadCoachServiceImpl implements HeadCoachService {
    private final HeadCoachRepository headCoachRepository;
    private final HeadCoachMapper headCoachMapper;
    private final TeamRepository teamRepository;
    @Override
    @Transactional
    public HeadCoachResponse create(HeadCoachCreateRequest request) {
        if(headCoachRepository.existsByName(request.name())){
            throw  new AlreadyExistsException("This coach already exists");
        }
        HeadCoach headCoach=headCoachMapper.toHeadCoach(request);
        HeadCoach savedHeadCoach=headCoachRepository.save(headCoach);
        HeadCoachResponse response=headCoachMapper.toHeadCoachResponse(savedHeadCoach);
        return response;
    }

    @Override
    public List<HeadCoachResponse> getAll() {
       List<HeadCoach> headCoaches= headCoachRepository.findAll();
        return headCoachMapper.toHeadCoachResponseList(headCoaches);
    }

    @Override
    public HeadCoachResponse update(HeadCoachUpdateRequest request, Integer id) {
        HeadCoach headCoach=headCoachRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Head coach not found"));

        if(headCoachRepository.existsByName(request.name())&&!headCoach.getName().equals(request.name())){
            throw new AlreadyExistsException("This head coach already exists");
        }

        headCoachMapper.updateEntityFromRequest(request,headCoach);

        headCoachRepository.save(headCoach);

        return headCoachMapper.toHeadCoachResponse(headCoach);
    }

    @Override
    public HeadCoachResponse getById(Integer id) {
        HeadCoach headCoach=headCoachRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Head coach not found"));
        return headCoachMapper.toHeadCoachResponse(headCoach);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        HeadCoach headCoach=headCoachRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Head coach not found"));


        headCoachRepository.delete(headCoach);
        teamRepository.unAssignTeamFromHeadCoach(id);
    }
}
