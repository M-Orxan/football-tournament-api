package com.example.football_tournament_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "players")
@SQLRestriction("is_deleted=false")
@SQLDelete(sql = "UPDATE players SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")

public class Player extends BaseEntity{
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")

    private Team team;
}
