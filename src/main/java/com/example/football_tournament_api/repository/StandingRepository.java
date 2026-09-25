package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.entity.Standing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface StandingRepository extends JpaRepository<Standing,Integer> {

    @Query("select s from Standing s where s.tournament.id=:tournamentId and s.team.id=:teamId")
    Optional<Standing> findByTournamentAndTeam(@Param("tournamentId")Integer tournamentId, @Param("teamId") Integer teamId);

    boolean existsByTournamentId(Integer tournamentId);
    
    List<Standing> findAllByTournamentIdOrderByPointsDescGoalDifferenceDescGoalsForDesc(Integer tournamentId);

    List<Standing> findAllByTournamentId(Integer tournamentId);

}
