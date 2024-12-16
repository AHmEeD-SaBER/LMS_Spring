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
    @Column(nullable = false)
    private Integer totalQuestions;

    @Column(nullable = false)
    private Integer passingScore;

}
