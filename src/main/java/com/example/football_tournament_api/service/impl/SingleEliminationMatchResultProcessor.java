package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.match.UpdateMatchScoreRequest;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.enums.MatchStatus;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.event.MatchFinishedEvent;
import com.example.football_tournament_api.exception.InvalidMatchScoreException;
import com.example.football_tournament_api.mapper.MatchMapper;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.service.MatchResultProcessor;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SingleEliminationMatchResultProcessor implements MatchResultProcessor {

    private final MatchMapper matchMapper;
    private final MatchRepository matchRepository;

    @Override
    public TournamentType getType() {
        return TournamentType.SingleElimination;
    }

    @Override
    @Transactional
    public void processMatch(Match match) {


    }
}
