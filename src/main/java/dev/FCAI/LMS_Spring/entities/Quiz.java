package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("QUIZ")
public class Quiz extends Assessment {
    @Column(name="total_Questions")
    private Integer totalQuestions;

    @Column(name = "passing_score")
    private Integer passingScore;
}
