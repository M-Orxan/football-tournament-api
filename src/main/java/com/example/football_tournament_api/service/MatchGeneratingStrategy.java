package com.example.football_tournament_api.service;

import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;

import java.util.List;

public interface MatchGeneratingStrategy {
    List<Match> generateMatches(Tournament tournament,List<Team> teams);
}
