package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match,Integer> {

    boolean existsByTournamentId(Integer tournamentId);
    @Query("select m from Match m join fetch m.homeTeam join fetch m.awayTeam where m.tournament.id=:tournamentId order by m.id")
    List<Match> findByTournamentId(@Param("tournamentId") Integer tournamentId);

    @Query("select m from Match m join fetch m.homeTeam join fetch m.awayTeam where m.id=:matchId")
    Optional<Match> findMatchById(@Param("matchId")Integer id);
}
