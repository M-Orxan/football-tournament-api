package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.entity.HeadCoach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeadCoachRepository extends JpaRepository<HeadCoach,Integer> {
        boolean existsByName(String name);
}
