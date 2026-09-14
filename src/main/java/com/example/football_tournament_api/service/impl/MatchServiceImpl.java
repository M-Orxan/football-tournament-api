package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.dto.match.MatchResponse;
import com.example.football_tournament_api.dto.match.UpdateMatchScoreRequest;
import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.MatchStatus;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.exception.InvalidMatchScoreException;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.mapper.MatchMapper;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.repository.TeamRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.service.MatchResultProcessor;
import com.example.football_tournament_api.service.MatchService;
import com.example.football_tournament_api.service.StandingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final MatchResultProcessorFactory matchResultProcessorFactory;
    private final TeamRepository teamRepository;


    @Override
    @Transactional
    public MatchResponse updateMatchScore(Integer matchId, UpdateMatchScoreRequest request) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        if (match.getTournament().getType() == TournamentType.SingleElimination) {
            boolean isNextRoundGenerated = matchRepository.existsByTournamentIdAndRoundNumberGreaterThan
                    (match.getTournament().getId(), match.getRoundNumber());

            if (isNextRoundGenerated) {
                throw new IllegalStateException("Next round for this tournament had already been generated. You can't edit for previous round");
            }
        }

        if ((request.homeTeamScore().equals(request.awayTeamScore()) &&
                match.getTournament().getType() == TournamentType.SingleElimination)) {
            throw new InvalidMatchScoreException("Invalid score. Can't be draw");
        } else if (request.homeTeamScore() > request.awayTeamScore()) {
            match.setWinnerTeamId(match.getHomeTeam().getId());
        } else if (request.awayTeamScore() > request.homeTeamScore()) {
            match.setWinnerTeamId(match.getAwayTeam().getId());
        }
        match.setStatus(MatchStatus.Finished);
        matchMapper.updateMatchScore(request, match);

        TournamentType type = match.getTournament().getType();
        MatchResultProcessor processor = matchResultProcessorFactory.getProcessor(type);
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

        List<Match> matches = matchRepository.findAllWithTeamsByTournamentId(tournamentId);
        List<MatchResponse> response = matchMapper.toMatchResponseList(matches);
        return response;
    }

    @Override
    public MatchResponse getMatchById(Integer matchId) {
        Match match = matchRepository.findMatchWithTeamsById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        return matchMapper.toMatchResponse(match);

    }

    @Override
    @Transactional
    public List<MatchResponse> simulateMatchesByRound(Integer tournamentId, int roundNumber) {

        List<Match> matches = matchRepository.findByTournamentIdAndRoundNumber(tournamentId, roundNumber);
        List<MatchResponse> responses = new ArrayList<>();

        for (Match match : matches) {
            int homeTeamScore = ThreadLocalRandom.current().nextInt(0, 4);
            int awayTeamScore = ThreadLocalRandom.current().nextInt(0, 4);

            if (match.getTournament().getType() == TournamentType.SingleElimination) {
                while (homeTeamScore == awayTeamScore) {
                    homeTeamScore = ThreadLocalRandom.current().nextInt(0, 4);
                    awayTeamScore = ThreadLocalRandom.current().nextInt(0, 4);
                }
            }
            UpdateMatchScoreRequest request = new UpdateMatchScoreRequest(
                    homeTeamScore,
                    awayTeamScore
            );

            responses.add(updateMatchScore(match.getId(), request));

        }

        return responses;


    }


@Override
@Transactional
    public List<MatchResponse> simulateAllMatchesByTournament(Integer tournamentId) {

        List<Match> matches = matchRepository.findAllWithTeamsByTournamentId(tournamentId);
        List<MatchResponse> responses = new ArrayList<>();

        for (Match match : matches) {
            int homeTeamScore = ThreadLocalRandom.current().nextInt(0, 5);
            int awayTeamScore = ThreadLocalRandom.current().nextInt(0, 5);

            if (match.getTournament().getType() == TournamentType.SingleElimination) {
                while (homeTeamScore == awayTeamScore) {
                    homeTeamScore = ThreadLocalRandom.current().nextInt(0, 5);
                    awayTeamScore = ThreadLocalRandom.current().nextInt(0, 5);
                }
            }
            UpdateMatchScoreRequest request = new UpdateMatchScoreRequest(
                    homeTeamScore,
                    awayTeamScore
            );

            responses.add(updateMatchScore(match.getId(), request));

        }

        return responses;


    }



    @Transactional
    @Override
    public List<MatchResponse> getMatchesByTeam(Integer tournamentId,Integer teamId) {

        if(!tournamentRepository.existsById(tournamentId)){
            throw new ResourceNotFoundException("Tournament not found");
        }
        if(!teamRepository.existsById(teamId)){
            throw new ResourceNotFoundException("Team not found");
        }
        List<Match> matches=matchRepository.findByTournamentIdAndTeamId(tournamentId,teamId);

        return matchMapper.toMatchResponseList(matches);

    }
}
