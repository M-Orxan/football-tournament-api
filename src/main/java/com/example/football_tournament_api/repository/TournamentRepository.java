package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TournamentRepository extends JpaRepository<Tournament,Integer> {
    boolean existsByName(String name);
}
