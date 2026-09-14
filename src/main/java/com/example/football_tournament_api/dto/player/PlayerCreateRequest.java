package com.example.football_tournament_api.dto.player;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record PlayerCreateRequest(
     @NotBlank(message = "Player name is required")
     String name,
     @NotNull(message = "team id is required")
     Integer teamId
) {
}
