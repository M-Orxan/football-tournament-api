package com.example.football_tournament_api.dto.tournament;

import com.example.football_tournament_api.dto.team.TeamSimpleResponse;
import com.example.football_tournament_api.enums.AggregationType;
import com.example.football_tournament_api.enums.StatType;


import java.util.List;

public record TournamentStatResponse(
        Integer tournamentId,
        StatType statType,
        AggregationType aggregationType,
        Double value,
        List<TeamSimpleResponse> teams
) {
}
