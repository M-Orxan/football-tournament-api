package com.example.football_tournament_api.entity;


import com.example.football_tournament_api.enums.MatchStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "matches")

public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "tournament_id",nullable = false)
    private Tournament tournament;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "home_team_id",nullable = false)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "away_team_id",nullable = false)
    private Team awayTeam;

    private Integer homeTeamScore;
    private Integer awayTeamScore;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchStatus status=MatchStatus.NotFinished;


    @Column(nullable = false)
    private int roundNumber;
    private int winnerTeamId;
}
