package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.dto.player.PlayerCreateRequest;
import com.example.football_tournament_api.dto.player.PlayerResponse;
import com.example.football_tournament_api.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player,Integer>
{

    boolean existsByName(String name);

}
