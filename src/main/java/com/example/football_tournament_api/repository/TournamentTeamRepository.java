package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.entity.Team;
import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.entity.TournamentTeam;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TournamentTeamRepository extends JpaRepository<TournamentTeam,Integer> {
    boolean existsByTournamentIdAndTeamId(Integer tournamentId, Integer teamId);

    @Query("Select tt.team from TournamentTeam tt where tt.tournament.id=:tournamentId")
    List<Team> findTeamsByTournamentId(@Param("tournamentId") Integer tournamentId);

    @Query("Select tt.team.id from TournamentTeam tt where tt.tournament.id=:tournamentId")
    List<Integer> findTeamIdsByTournamentId(@Param("tournamentId") Integer tournamentId);
}
