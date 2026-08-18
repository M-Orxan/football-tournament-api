package com.example.football_tournament_api.service.impl;

import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.service.MatchGeneratingStrategy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class RoundRobinMatchGenerationStrategy implements MatchGeneratingStrategy {

    @Override
    public List<Match> generateMatches(Tournament tournament, List<Team> teams) {
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
                matches.add(match);
            }

            List<Team> sub = list.subList(1, totalTeams);
            Collections.rotate(sub, 1);
        }
        return matches;
    }
}
