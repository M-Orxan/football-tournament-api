package com.example.football_tournament_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "standings")
@SQLRestriction("is_deleted=false")
@SQLDelete(sql = "UPDATE standings SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")

public class Standing extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id",nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id",nullable = false)
    private Tournament tournament;

    private int played=0;
    private int won=0;
    private int drawn=0;
    private int lost=0;
    private int goalsFor=0;
    private int goalsAgainst=0;
    private int goalDifference=0;
    private int points=0;
    private int cleanSheet=0;


}
