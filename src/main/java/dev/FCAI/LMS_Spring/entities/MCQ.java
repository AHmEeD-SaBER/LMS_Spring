package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MCQ")
@Getter
@Setter
public class MCQ extends Question {
    @ElementCollection
    @CollectionTable(name = "mcq_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option")
    private List<String> options = new ArrayList<>();

    @Override
    public double gradeQuestion(String submittedAnswer) {
        if (this.correctAnswer.equals(submittedAnswer)) {
            return this.grade;
        } else {
            return 0.0;
        }
    }
}
