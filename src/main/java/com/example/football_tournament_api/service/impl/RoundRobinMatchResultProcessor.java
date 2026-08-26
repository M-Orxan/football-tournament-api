package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.match.UpdateMatchScoreRequest;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.enums.MatchStatus;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.event.MatchFinishedEvent;
import com.example.football_tournament_api.mapper.MatchMapper;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.service.MatchResultProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoundRobinMatchResultProcessor implements MatchResultProcessor {
   private final ApplicationEventPublisher applicationEventPublisher;
   private final MatchRepository matchRepository;
   private final MatchMapper matchMapper;
    @Override
    public TournamentType getType() {
        return TournamentType.RoundRobin;
    }

    @Override
    public void processMatch(Match match) {

        applicationEventPublisher.publishEvent(new MatchFinishedEvent(
                match.getTournament().getId()
        ));
    }


}
