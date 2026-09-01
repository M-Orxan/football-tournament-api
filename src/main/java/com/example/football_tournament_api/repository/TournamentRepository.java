package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.entity.Tournament;
import com.example.football_tournament_api.enums.TournamentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TournamentRepository extends JpaRepository<Tournament,Integer> {
    boolean existsByName(String name);
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Tournament t WHERE t.id = :id AND t.type = :type")
    boolean isGivenType(@Param("id")Integer tournamentId, @Param("type") TournamentType type);
}
