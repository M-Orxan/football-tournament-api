package com.example.football_tournament_api.repository;


import com.example.football_tournament_api.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Integer>
{

    boolean existsByName(String name);

    @Modifying(clearAutomatically = true)
    @Query("Update Player p set p.team.id=null where p.team.id=:teamId")
    void unassignPlayersFromTeam(@Param("teamId")Integer teamId);//komandani sof delete sildikden sonra
}
