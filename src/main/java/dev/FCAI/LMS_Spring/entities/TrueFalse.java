package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("TRUE_FALSE")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class TrueFalse extends Question {
    @Override
    public double gradeQuestion(String submittedAnswer) {
        if (this.correctAnswer.equalsIgnoreCase(submittedAnswer)) {
            return this.grade;
        } else {
            return 0.0;
        }
    }
}
