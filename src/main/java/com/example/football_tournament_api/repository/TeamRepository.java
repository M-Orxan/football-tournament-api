package com.example.football_tournament_api.repository;

import com.example.football_tournament_api.dto.team.TeamResponse;
import com.example.football_tournament_api.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team,Integer> {

    boolean existsByName(String name);

    @Query("""
        SELECT new com.example.football_tournament_api.dto.team.TeamResponse(
            t.id, 
            t.name, 
                  t.headCoach.id,
                    t.headCoach.name,
            COUNT(p.id) ,
                t.createdAt   
        )
        FROM Team t 
        LEFT JOIN t.players p
        WHERE t.id = :id
        GROUP BY t.id, t.name,t.headCoach.id,t.headCoach.name, t.createdAt
    """)
    Optional<TeamResponse> findTeamWithPlayerCountById(@Param("id") Integer id);


    @Query("""
        SELECT new com.example.football_tournament_api.dto.team.TeamResponse(
            t.id, 
            t.name, 
                t.headCoach.id,
                    t.headCoach.name,
            COUNT(p.id),
             t.createdAt
                
        )
        FROM Team t 
        LEFT JOIN t.players p
        GROUP BY t.id, t.name,t.headCoach.id,t.headCoach.name,t.createdAt
    """)
    List<TeamResponse> findTeamsWithPlayerCount();


    boolean existsByHeadCoachId(Integer id);

    @Modifying
    @Query("Update Team t set t.headCoach.id=null where t.headCoach.id=:headCoachId")
    void unAssignTeamFromHeadCoach(@Param("headCoachId") Integer headCoachId);//head coach soft delete edildikden sonra
}
