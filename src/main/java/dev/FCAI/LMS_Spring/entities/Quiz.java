package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
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
    @JsonView(Views.Summary.class)
    private Integer totalQuestions;

    @Column(name = "passing_score")
    @JsonView(Views.Summary.class)
    private Integer passingScore;
}
