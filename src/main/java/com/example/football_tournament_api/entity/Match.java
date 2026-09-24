package com.example.football_tournament_api.entity;


import com.example.football_tournament_api.enums.MatchStatus;
import com.example.football_tournament_api.enums.TournamentType;
import com.example.football_tournament_api.exception.InvalidMatchScoreException;
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


    public void finishMatch(Integer homeScore, Integer awayScore) {
        if (homeScore == null || awayScore == null) {
            throw new IllegalArgumentException("Match scores cannot be null");
        }

        if (this.tournament.getType() == TournamentType.SingleElimination && homeScore.equals(awayScore)) {
            throw new InvalidMatchScoreException("Invalid score. Can't be draw in single elimination");
        }

        if (homeScore > awayScore) {
            this.winnerTeamId = this.homeTeam.getId();
        } else if (awayScore > homeScore) {
            this.winnerTeamId = this.awayTeam.getId();
        }
        this.status = MatchStatus.Finished;
    }
}
