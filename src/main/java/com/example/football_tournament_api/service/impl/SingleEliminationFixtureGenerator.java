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
    public List<Match> generateFixture(Tournament tournament) {

        if (matchRepository.existsByTournamentIdAndRoundNumberGreaterThan(tournament.getId(), 1)) {
            throw new AlreadyExistsException("This round had already finished");
        }

        if (matchRepository.existsByTournamentIdAndRoundNumber(tournament.getId(), 1)) {
            throw new AlreadyExistsException("Fixture of this round 1 had already been generated");
        }


        List<Team> registeredTeams = tournamentTeamRepository.findTeamsByTournamentId(tournament.getId());
        List<Match> matches = generateSingleEliminationMatches(tournament, registeredTeams, 1);
        matchRepository.saveAll(matches);
        return matchRepository.findByTournamentId(tournament.getId());
    }

    private List<Match> generateSingleEliminationMatches(Tournament tournament, List<Team> teams, int roundNumber) {

        List<Team> availableTeams = new ArrayList<>(teams);
        List<Match> matches = new ArrayList<>();

        Collections.shuffle(availableTeams);
        for (int i = 0; i < availableTeams.size(); i += 2) {
            Team homeTeam = availableTeams.get(i);
            Team awayTeam = availableTeams.get(i + 1);

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
    public List<Match> generateNextRound(Integer tournamentId, Integer nextRoundNumber) {

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));

        if (matchRepository.existsByTournamentIdAndRoundNumber(tournament.getId(), nextRoundNumber)) {
            throw new AlreadyExistsException("Fixture of round " + nextRoundNumber + " had already been generated");
        }

        List<Match> lastRoundMatches = matchRepository.findByTournamentIdAndRoundNumber(tournamentId, nextRoundNumber - 1);
        if (lastRoundMatches.isEmpty()) {
            throw new IllegalStateException("Invalid round number");
        }


        boolean isLastRoundFinished = lastRoundMatches.stream()
                .allMatch(m -> m.getStatus() == MatchStatus.Finished);

        if (!isLastRoundFinished) {
            throw new RoundNotCompletedException("Last round " + (nextRoundNumber - 1) + " has not been completed yet");
        }

        List<Integer> winnerTeamIds = lastRoundMatches.stream()
                .map(Match::getWinnerTeamId)
                .filter(Objects::nonNull)
                .toList();
        List<Team> winnerTeams = teamRepository.findAllById(winnerTeamIds);

        if (winnerTeams.size() < 2) {
            throw new IllegalStateException("Tournament already finished");
        }

        List<Match> nextRoundMatches = generateSingleEliminationMatches(tournament, winnerTeams, nextRoundNumber);

        matchRepository.saveAll(nextRoundMatches);
        return matchRepository.findByTournamentId(tournamentId);
    }

}
