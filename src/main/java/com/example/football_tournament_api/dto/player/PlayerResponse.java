package com.example.football_tournament_api.dto.player;

import java.time.LocalDateTime;

public record PlayerResponse(
        Integer id,
        String name,
        boolean deleted,
        Integer teamId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime deletedAt
) {
}
