package com.example.football_tournament_api.controller;

import com.example.football_tournament_api.dto.head_coach.HeadCoachCreateRequest;
import com.example.football_tournament_api.dto.head_coach.HeadCoachResponse;
import com.example.football_tournament_api.dto.head_coach.HeadCoachUpdateRequest;
import com.example.football_tournament_api.dto.team.TeamResponse;
import com.example.football_tournament_api.service.HeadCoachService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/head-coaches")
@RequiredArgsConstructor
public class HeadCoachController {
    private final HeadCoachService headCoachService;

    @PostMapping
    public ResponseEntity<HeadCoachResponse> create(@RequestBody HeadCoachCreateRequest request){
        return  ResponseEntity.status(HttpStatus.CREATED).body(headCoachService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<HeadCoachResponse>> getAll(){
        return  ResponseEntity.status(HttpStatus.OK).body(headCoachService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeadCoachResponse> getById(@PathVariable Integer id){
        return  ResponseEntity.status(HttpStatus.OK).body(headCoachService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        headCoachService.delete(id);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<HeadCoachResponse> update(@RequestBody HeadCoachUpdateRequest request, @PathVariable Integer id){

        return  ResponseEntity.status(HttpStatus.OK).body(headCoachService.update(request,id));

    }

}
