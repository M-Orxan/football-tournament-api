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
public class HeadCoachImplTest {

    @Mock
    private HeadCoachRepository headCoachRepository;

    @Mock
    private HeadCoachMapper headCoachMapper;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private HeadCoachServiceImpl headCoachService;

    @Test
    void create_ShouldReturnHeadCoachResponse_WhenEverythingIsValid() {
        //arrange
        Integer id = 1;
        String name = "head coach name";
        LocalDateTime createdAt = LocalDateTime.now();
        HeadCoachCreateRequest request = new HeadCoachCreateRequest(name);
        HeadCoach mapped = new HeadCoach();
        mapped.setName(name);
        mapped.setCreatedAt(createdAt);
        HeadCoach saved = new HeadCoach();
        saved.setId(id);
        saved.setName(name);
        saved.setCreatedAt(createdAt);
        HeadCoachResponse expectedResponse = new HeadCoachResponse(id, name, createdAt);

        //behaviour
        when(headCoachRepository.existsByName(request.name())).thenReturn(false);
        when(headCoachMapper.toHeadCoach(request)).thenReturn(mapped);
        when(headCoachRepository.save(mapped)).thenReturn(saved);
        when(headCoachMapper.toHeadCoachResponse(saved)).thenReturn(expectedResponse);

        //act
        HeadCoachResponse actualResponse=headCoachService.create(request);

        //assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.name(),actualResponse.name());
        assertEquals(expectedResponse.id(),actualResponse.id());
        assertEquals(expectedResponse.createdAt(),actualResponse.createdAt());

        //verify
        verify(headCoachRepository,times(1)).existsByName(name);
        verify(headCoachMapper,times(1)).toHeadCoach(request);
        verify(headCoachRepository,times(1)).save(mapped);
        verify(headCoachMapper,times(1)).toHeadCoachResponse(saved);



    }

    @Test
    void create_ShouldThrowAlreadyExistsException_WhenCoachNameExists(){
        //arrange
        HeadCoachCreateRequest request = new HeadCoachCreateRequest("head coach name");

        when(headCoachRepository.existsByName(request.name())).thenReturn(true);

        AlreadyExistsException exception=assertThrows(AlreadyExistsException.class,
                ()->headCoachService.create(request));

        assertEquals("This coach already exists",exception.getMessage());

        verify(headCoachRepository, times(1)).existsByName(request.name());
        verify(headCoachMapper, never()).toHeadCoach(any());
        verify(headCoachRepository, never()).save(any());
        verify(headCoachMapper, never()).toHeadCoachResponse(any());

    }

    @Test
    void getAll_ShouldReturnListOfResponses() {
        // Arrange
        HeadCoach headCoach = new HeadCoach();
        headCoach.setId(1);
        headCoach.setName("head coach");

        HeadCoachResponse response = new HeadCoachResponse(1, "head coach", null);

        List<HeadCoach> headCoaches = List.of(headCoach);
        List<HeadCoachResponse> expectedResponses = List.of(response);

        when(headCoachRepository.findAll()).thenReturn(headCoaches);
        when(headCoachMapper.toHeadCoachResponseList(headCoaches)).thenReturn(expectedResponses);

        // Act
        List<HeadCoachResponse> actualResponses = headCoachService.getAll();

        // Assert
        assertNotNull(actualResponses);
        assertEquals(1, actualResponses.size());
        assertEquals(expectedResponses.get(0).name(), actualResponses.get(0).name());

        verify(headCoachRepository, times(1)).findAll();
        verify(headCoachMapper, times(1)).toHeadCoachResponseList(headCoaches);
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoCoachesExist() {
        when(headCoachRepository.findAll()).thenReturn(Collections.emptyList());
        when(headCoachMapper.toHeadCoachResponseList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<HeadCoachResponse> actualResponses = headCoachService.getAll();

        assertTrue(actualResponses.isEmpty());
        verify(headCoachRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnResponse_WhenCoachExists() {
        Integer coachId = 1;
        HeadCoach headCoach = new HeadCoach();
        headCoach.setId(coachId);

        HeadCoachResponse expectedResponse = new HeadCoachResponse(coachId, "Name", null);

        when(headCoachRepository.findById(coachId)).thenReturn(Optional.of(headCoach));
        when(headCoachMapper.toHeadCoachResponse(headCoach)).thenReturn(expectedResponse);

        HeadCoachResponse actualResponse = headCoachService.getById(coachId);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.id(), actualResponse.id());
        verify(headCoachRepository, times(1)).findById(coachId);
    }

    @Test
    void getById_ShouldThrowNotFoundException_WhenCoachDoesNotExist() {
        Integer coachId = 99;
        when(headCoachRepository.findById(coachId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> headCoachService.getById(coachId));

        assertEquals("Head coach not found", exception.getMessage());
        verify(headCoachMapper, never()).toHeadCoachResponse(any());
    }

    @Test
    void delete_ShouldDeleteCoachAndUnassignTeam_WhenCoachExists() {
        Integer coachId = 1;
        HeadCoach headCoach = new HeadCoach();
        headCoach.setId(coachId);

        when(headCoachRepository.findById(coachId)).thenReturn(Optional.of(headCoach));

        // Act
        headCoachService.delete(coachId);

        // Assert
        verify(headCoachRepository, times(1)).findById(coachId);
        verify(headCoachRepository, times(1)).delete(headCoach);
        verify(teamRepository, times(1)).unAssignTeamFromHeadCoach(coachId);
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenCoachDoesNotExist() {
        Integer coachId = 99;
        when(headCoachRepository.findById(coachId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> headCoachService.delete(coachId));

        assertEquals("Head coach not found", exception.getMessage());

        verify(headCoachRepository, never()).delete(any());
        verify(teamRepository, never()).unAssignTeamFromHeadCoach(anyInt());
    }

    @Test
    void update_ShouldUpdateAndReturnResponse_WhenCoachExists() {
        // Arrange
        Integer coachId = 1;
        HeadCoachUpdateRequest request = new HeadCoachUpdateRequest("Jurgen Klopp");

        HeadCoach existingCoach = new HeadCoach();
        existingCoach.setId(coachId);
        existingCoach.setName("Old Name");

        HeadCoachResponse expectedResponse = new HeadCoachResponse(coachId, "Jurgen Klopp", null);

        when(headCoachRepository.findById(coachId)).thenReturn(Optional.of(existingCoach));


        doAnswer(invocation -> {
            HeadCoachUpdateRequest req = invocation.getArgument(0);
            HeadCoach coach = invocation.getArgument(1);
            coach.setName(req.name());
            return null;
        }).when(headCoachMapper).updateEntityFromRequest(request, existingCoach);

        when(headCoachRepository.save(existingCoach)).thenReturn(existingCoach);
        when(headCoachMapper.toHeadCoachResponse(existingCoach)).thenReturn(expectedResponse);

        // Act
        HeadCoachResponse actualResponse = headCoachService.update(request, coachId);

        // Assert
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.name(), actualResponse.name());

        verify(headCoachRepository, times(1)).findById(coachId);
        verify(headCoachMapper, times(1)).updateEntityFromRequest(request, existingCoach);
        verify(headCoachRepository, times(1)).save(existingCoach);
        verify(headCoachMapper, times(1)).toHeadCoachResponse(existingCoach);
    }

    @Test
    void update_ShouldThrowResourceNotFoundException_WhenCoachDoesNotExist() {
        // Arrange
        Integer coachId = 99;
        HeadCoachUpdateRequest request = new HeadCoachUpdateRequest("Jurgen Klopp");

        when(headCoachRepository.findById(coachId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> headCoachService.update(request, coachId));

        assertEquals("Head coach not found", exception.getMessage());


        verify(headCoachMapper, never()).updateEntityFromRequest(any(), any());
        verify(headCoachRepository, never()).save(any());
        verify(headCoachMapper, never()).toHeadCoachResponse(any());
    }



}
