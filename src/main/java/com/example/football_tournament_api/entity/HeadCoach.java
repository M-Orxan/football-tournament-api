package com.example.football_tournament_api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "head_coaches")
@SQLRestriction("is_deleted=false")
@SQLDelete(sql = "UPDATE head_coaches SET is_deleted = true, deleted_at = CURRENT_TIMESTAMP WHERE id=?")

public class HeadCoach extends BaseEntity {
    @Column(nullable = false)
    private String name;
}
