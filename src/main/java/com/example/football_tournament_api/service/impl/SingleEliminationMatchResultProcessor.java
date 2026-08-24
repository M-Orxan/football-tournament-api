package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.enums.MatchStatus;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.event.MatchFinishedEvent;
import com.example.football_tournament_api.exception.InvalidMatchScoreException;
import com.example.football_tournament_api.service.MatchResultProcessor;
import org.springframework.stereotype.Service;

@Service
public class SingleEliminationMatchResultProcessor implements MatchResultProcessor {

    @Override
    public TournamentType getType() {
        return TournamentType.SingleElimination;
    }

    @Override
    public void processMatch(Match match) {

        if(match.getHomeTeamScore().equals(match.getAwayTeamScore())){
            throw new InvalidMatchScoreException("Invalid score. Can't be draw");
        }



    }
}
