package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.dto.match.UpdateMatchScoreRequest;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.MatchStatus;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.event.MatchFinishedEvent;
import com.example.football_tournament_api.exception.InvalidMatchScoreException;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.MatchMapper;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.service.MatchResultProcessor;
import com.example.football_tournament_api.service.MatchService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final MatchResultProcessorFactory matchResultProcessorFactory;

    @Override
    @Transactional
    public MatchResponse updateMatchScore(Integer matchId, UpdateMatchScoreRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        matchMapper.updateMatchScore(request, match);
        match.setStatus(MatchStatus.Finished);
        if(request.homeTeamScore()> request.awayTeamScore()){
            match.setWinnerTeamId(match.getHomeTeam().getId());
        }
        else if(request.awayTeamScore()>request.homeTeamScore()){
            match.setWinnerTeamId(match.getAwayTeam().getId());
        }
        TournamentType type=match.getTournament().getType();
        MatchResultProcessor processor=matchResultProcessorFactory.getProcessor(type);
        processor.processMatch(match);


        return matchMapper.toMatchResponse(match);
    }


    @Override
    public List<MatchResponse> getMatchesByTournamentId(Integer tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

        if (!matchRepository.existsByTournamentId(tournamentId)) {
            throw new ResourceNotFoundException("Matches have not still been generated for this tournament");
        }

        List<Match> matches = matchRepository.findByTournamentId(tournamentId);
        List<MatchResponse> response = matchMapper.toMatchResponseList(matches);
        return response;
    }

    @Override
    public MatchResponse getMatchById(Integer matchId) {
        Match match = matchRepository.findMatchById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        return matchMapper.toMatchResponse(match);

    }

}
