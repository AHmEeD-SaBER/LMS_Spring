package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public abstract class BaseSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double totalScore;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    private boolean isGraded;



}