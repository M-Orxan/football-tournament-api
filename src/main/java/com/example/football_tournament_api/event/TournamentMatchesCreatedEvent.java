package com.example.football_tournament_api.event;

import java.util.List;

public record TournamentMatchesCreatedEvent(
        Integer tournamentId,
        List<Integer> teamIds
) {
}
