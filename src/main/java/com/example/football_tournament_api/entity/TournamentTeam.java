package com.example.football_tournament_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "tournament_teams")
@SQLRestriction("is_deleted=false")
@SQLDelete(sql = "UPDATE tournament_team SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")

public class TournamentTeam extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id",nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id",nullable = false)
    private Tournament tournament;

}
