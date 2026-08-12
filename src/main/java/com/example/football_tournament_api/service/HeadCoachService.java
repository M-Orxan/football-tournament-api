package com.example.football_tournament_api.service;

import com.example.football_tournament_api.dto.head_coach.HeadCoachCreateRequest;
import com.example.football_tournament_api.dto.head_coach.HeadCoachResponse;
import com.example.football_tournament_api.dto.head_coach.HeadCoachUpdateRequest;

public interface HeadCoachService extends GenericService<HeadCoachCreateRequest, HeadCoachUpdateRequest, HeadCoachResponse,Integer>{
}
