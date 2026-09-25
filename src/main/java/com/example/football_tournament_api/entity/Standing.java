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

public class Standing extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    private int played = 0;
    private int won = 0;
    private int drawn = 0;
    private int lost = 0;
    private int goalsFor = 0;
    private int goalsAgainst = 0;
    private int goalDifference = 0;
    private int points = 0;
    private int cleanSheet = 0;

    public void applyMatchResult(int goalsScored, int goalsConceded ) {

        this.played += 1;
        this.goalsFor += goalsScored;
        this.goalsAgainst += goalsConceded;
        this.goalDifference = this.goalsFor - this.goalsAgainst;

        // Clean sheet yoxlaması
        if (goalsConceded == 0) {
            this.cleanSheet += 1;
        }

        // Qalibiyyət, məğlubiyyət, xal və heç-heçə təyini
        if (goalsScored > goalsConceded) {
            this.won += 1;
            this.points += 3;
        } else if (goalsScored < goalsConceded) {
            this.lost += 1;
        } else {
            this.drawn += 1;
            this.points += 1;
        }
//        if(match.getHomeTeamScore()>match.getAwayTeamScore()){
//            homeTeamStanding.setWon(homeTeamStanding.getWon()+1);
//            awayTeamStanding.setLost(awayTeamStanding.getLost()+1);
//            homeTeamStanding.setPoints(homeTeamStanding.getPoints()+3);
//        }else if(match.getAwayTeamScore()>match.getHomeTeamScore()){
//            awayTeamStanding.setWon(awayTeamStanding.getWon()+1);
//            homeTeamStanding.setLost(homeTeamStanding.getLost()+1);
//            awayTeamStanding.setPoints(awayTeamStanding.getPoints()+3);
//        }
//        else{
//            homeTeamStanding.setDrawn(homeTeamStanding.getDrawn()+1);
//            awayTeamStanding.setDrawn(awayTeamStanding.getDrawn()+1);
//            homeTeamStanding.setPoints(homeTeamStanding.getPoints()+1);
//            awayTeamStanding.setPoints(awayTeamStanding.getPoints()+1);
//        }
//
//        if(match.getHomeTeamScore()==0){
//            awayTeamStanding.setCleanSheet(awayTeamStanding.getCleanSheet()+1);
//        }
//
//        if(match.getAwayTeamScore()==0){
//            homeTeamStanding.setCleanSheet(homeTeamStanding.getCleanSheet()+1);
//        }
//
//        homeTeamStanding.setGoalsFor(homeTeamStanding.getGoalsFor()+match.getHomeTeamScore());
//        homeTeamStanding.setGoalsAgainst(homeTeamStanding.getGoalsAgainst()+match.getAwayTeamScore());
//        homeTeamStanding.setGoalDifference(homeTeamStanding.getGoalsFor()- homeTeamStanding.getGoalsAgainst());
//        homeTeamStanding.setPlayed(homeTeamStanding.getPlayed()+1);
//
//        awayTeamStanding.setGoalsFor(awayTeamStanding.getGoalsFor()+match.getAwayTeamScore());
//        awayTeamStanding.setGoalsAgainst(awayTeamStanding.getGoalsAgainst()+match.getHomeTeamScore());
//        awayTeamStanding.setGoalDifference(awayTeamStanding.getGoalsFor()- awayTeamStanding.getGoalsAgainst());
//        awayTeamStanding.setPlayed(awayTeamStanding.getPlayed()+1);
    }

    public void reset() {
        this.played = 0;
        this.won = 0;
        this.lost = 0;
        this.drawn = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.goalDifference = 0;
        this.points = 0;
        this.cleanSheet = 0;
    }


}
