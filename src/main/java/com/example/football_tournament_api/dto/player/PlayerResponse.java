package com.example.football_tournament_api.dto.player;

import java.time.LocalDateTime;

public record PlayerResponse(
        Integer id,
        String name,
        Integer teamId,
        String teamName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
