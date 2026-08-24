package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.MatchStatus;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.exception.AlreadyExistsException;
import com.example.football_tournament_api.exception.ResourceNotFoundException;
import com.example.football_tournament_api.exception.RoundNotCompletedException;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.repository.TeamRepository;
import com.example.football_tournament_api.repository.TournamentRepository;
import com.example.football_tournament_api.repository.TournamentTeamRepository;
import com.example.football_tournament_api.service.FixtureGeneratorStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SingleEliminationFixtureGenerator implements FixtureGeneratorStrategy {
    private final MatchRepository matchRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;

    @Override
    public TournamentType getType() {
        return TournamentType.SingleElimination;
    }

    @Override
    public void generateFixture(Tournament tournament) {
        if (matchRepository.existsByTournamentIdAndStatus(tournament.getId(), MatchStatus.NotFinished)) {
            throw new AlreadyExistsException("Matches of current round have not been completed yet");
        }

        List<Team> registeredTeams = tournamentTeamRepository.findTeamsByTournamentId(tournament.getId());
        List<Match> matches = generateSingleEliminationMatches(tournament, registeredTeams,1);
        matchRepository.saveAll(matches);
    }

    private List<Match> generateSingleEliminationMatches(Tournament tournament, List<Team> teams, int roundNumber) {

        List<Team> availableTeams = new ArrayList<>(teams);
        List<Match> matches = new ArrayList<>();
        int matchCount = availableTeams.size() / 2;
        Collections.shuffle(availableTeams);
        for (int i = 0; i < matchCount; i++) {
            int homeTeamIndex = ThreadLocalRandom.current().nextInt(0, availableTeams.size());
            Team homeTeam = availableTeams.get(homeTeamIndex);
            availableTeams.remove(homeTeam);

            int awayTeamIndex = ThreadLocalRandom.current().nextInt(0, availableTeams.size());
            Team awayTeam = availableTeams.get(awayTeamIndex);
            availableTeams.remove(awayTeam);

            Match match = new Match();
            match.setTournament(tournament);
            match.setHomeTeam(homeTeam);
            match.setAwayTeam(awayTeam);
            match.setRoundNumber(roundNumber);
            matches.add(match);
        }
        return matches;
    }

@Override
    public void generateNextRound(Integer tournamentId, Integer nextRoundNumber) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));


        List<Match> currentMatches = matchRepository.findByTournamentIdAndRoundNumber(tournamentId, nextRoundNumber-1);
        if(currentMatches.isEmpty()){
            throw new IllegalStateException("Invalid round number");
        }


        boolean isCurrentRoundFinished = currentMatches.stream()
                .allMatch(m -> m.getStatus() == MatchStatus.Finished);

        if (!isCurrentRoundFinished) {
            throw new RoundNotCompletedException("Round " + (nextRoundNumber-1) + " has not been completed yet");
        }

        List<Integer> winnerTeamIds=currentMatches.stream()
                .map(Match::getWinnerTeamId)
                .filter(Objects::nonNull)
                .toList();
        List<Team> winnerTeams=teamRepository.findAllById(winnerTeamIds);

        if(winnerTeams.size()<2){
            throw new IllegalStateException("Tournament already finished");
        }

        List<Match> nextRoundMatches=generateSingleEliminationMatches(tournament,winnerTeams,nextRoundNumber);

        matchRepository.saveAll(nextRoundMatches);
    }

}
