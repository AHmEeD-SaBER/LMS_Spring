package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("ESSAY")
@Getter
@Setter
public class shortAnswer extends Question {
    @Override
    public double gradeQuestion(String submittedAnswer) {
        if (this.correctAnswer.equalsIgnoreCase(submittedAnswer.trim())) {
            return this.grade;
        } else {
            return 0.0;
        }
    }
}
