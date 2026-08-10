package com.example.football_tournament_api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "teams")
@SQLRestriction("is_deleted=false")
public class Team extends BaseEntity {

    @Column(nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy = "team")
    private List<Player> players=new ArrayList<>();

    public void addPlayer(Player player){
        players.add(player);
        player.setTeam(this);
    }
    public void removePlayer(Player player){
        players.remove(player);
        player.setTeam(null);
    }
}
