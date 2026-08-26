package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.event.TournamentMatchesCreatedEvent;
import com.example.football_tournament_api.exception.AlreadyExistsException;
import com.example.football_tournament_api.repository.MatchRepository;
import com.example.football_tournament_api.repository.TournamentTeamRepository;
import com.example.football_tournament_api.service.FixtureGeneratorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoundRobinFixtureGenerator implements FixtureGeneratorStrategy {
    private final MatchRepository matchRepository;
    private final TournamentTeamRepository tournamentTeamRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public TournamentType getType() {
        return TournamentType.RoundRobin;
    }

    @Override
    public List<Match> generateFixture(Tournament tournament) {
        if(matchRepository.existsByTournamentId(tournament.getId())){
            throw new AlreadyExistsException("Matches of this tournament have already been generated");
        }

        List<Team> registeredTeams = tournamentTeamRepository.findTeamsByTournamentId(tournament.getId());
        List<Match> matches = generateRoundRobinMatches(tournament, registeredTeams);
        List<Integer> teamIds=tournamentTeamRepository.findTeamIdsByTournamentId(tournament.getId());
        applicationEventPublisher.publishEvent(new TournamentMatchesCreatedEvent(tournament.getId(),teamIds));
        matchRepository.saveAll(matches);
       return matchRepository.findByTournamentId(tournament.getId());
    }

    private List<Match> generateRoundRobinMatches(Tournament tournament, List<Team> teams) {
        int totalTeams = teams.size();
        int rounds = totalTeams - 1;
        int matchesPerRound = totalTeams / 2;

        List<Team> list = new ArrayList<>(teams);
        List<Match> matches = new ArrayList<>();
        for (int round = 0; round < rounds; round++) {

            for (int i = 0; i < matchesPerRound; i++) {
                Team homeTeam = list.get(i);
                Team awayTeam = list.get(totalTeams - 1 - i);
                Match match = new Match();
                match.setHomeTeam(homeTeam);
                match.setAwayTeam(awayTeam);
                match.setTournament(tournament);
                match.setRoundNumber(round+1);
                matches.add(match);
            }

            List<Team> sub = list.subList(1, totalTeams);
            Collections.rotate(sub, 1);
        }
        return matches;
    }
}
