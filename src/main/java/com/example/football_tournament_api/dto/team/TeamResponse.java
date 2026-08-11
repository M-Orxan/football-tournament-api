package com.example.football_tournament_api.dto.team;

import java.time.LocalDateTime;

public record TeamResponse(
        Integer id,
        String name,
        Long playerCount,
        LocalDateTime createdAt

) {
}
