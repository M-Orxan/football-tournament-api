package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team,Integer> {


}
