package com.example.football_tournament_api.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "teams")
@SQLRestriction("is_deleted=false")
@SQLDelete(sql = "UPDATE teams SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")

public class Team extends BaseEntity {

    @Column(nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Player> players=new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "head_coach_id")
    private HeadCoach headCoach;

    @OneToMany(mappedBy = "team",cascade=CascadeType.ALL,orphanRemoval = true)
    private List<TournamentTeam> tournamentTeams=new ArrayList<>();

    public void addPlayer(Player player){
        players.add(player);
        player.setTeam(this);
    }
    public void removePlayer(Player player){
        players.remove(player);
        player.setTeam(null);
    }
}
