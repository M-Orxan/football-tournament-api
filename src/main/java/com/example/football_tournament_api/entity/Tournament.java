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
@Table(name = "tournaments")
@SQLRestriction("is_deleted=false")
@SQLDelete(sql = "UPDATE tournaments SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")

public class Tournament extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "tournament",cascade= CascadeType.ALL,orphanRemoval = true)
    private List<TournamentTeam> tournamentTeams=new ArrayList<>();

}
