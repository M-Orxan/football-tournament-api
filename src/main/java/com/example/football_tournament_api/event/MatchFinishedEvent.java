package com.example.football_tournament_api.event;

public record MatchFinishedEvent(
        Integer tournamentId,
        Integer homeTeamId,
        Integer awayTeamId,
        int homeTeamScore,
        int awayTeamScore
) {
}
