package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.entity.Match;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Integer> {

    boolean existsByTournamentId(Integer tournamentId);
    @Query("select m from Match m join fetch m.homeTeam join fetch m.awayTeam where m.tournament.id=:tournamentId")
    List<Match> findByTournamentId(@Param("tournamentId") Integer tournamentId);
}
