package com.example.football_tournament_api.entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name = "is_deleted", nullable = false)
    boolean deleted=false;

    @Column(name = "created_at",nullable = false,updatable = false)
    @CreatedDate
    LocalDateTime createdAt=LocalDateTime.now();

    @Column(name = "deleted_at",nullable = false)
    LocalDateTime deletedAt;

    @Column(name = "updated_at",nullable = false,updatable = false)
    @LastModifiedDate
    LocalDateTime updatedAt;

}
