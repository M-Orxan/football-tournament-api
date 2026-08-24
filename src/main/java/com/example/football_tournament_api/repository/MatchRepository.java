package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.entity.Match;
import com.example.football_tournament_api.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends JpaRepository<Match,Integer> {

    boolean existsByTournamentId(Integer tournamentId);
    boolean existsByTournamentIdAndStatus(Integer tournamentId,MatchStatus status);
    @Query("select m from Match m join fetch m.homeTeam join fetch m.awayTeam where m.tournament.id=:tournamentId order by m.id")
    List<Match> findByTournamentId(@Param("tournamentId") Integer tournamentId);

    @Query("select m from Match m join fetch m.homeTeam join fetch m.awayTeam where m.id=:matchId")
    Optional<Match> findMatchById(@Param("matchId")Integer id);

    @Query("select m from Match m join fetch m.homeTeam join fetch m.awayTeam where m.tournament.id=:tournamentId and m.status=:status order by m.id")
    List<Match> findByTournamentIdAndStatus(@Param("tournamentId") Integer tournamentId, @Param("status")MatchStatus status);

    @Query("select m from Match m join fetch m.homeTeam join fetch m.awayTeam where m.tournament.id=:tournamentId and m.roundNumber=:roundNumber order by m.id")
    List<Match> findByTournamentIdAndRoundNumber(@Param("tournamentId") Integer tournamentId, @Param("roundNumber")int roundNumber);



}
