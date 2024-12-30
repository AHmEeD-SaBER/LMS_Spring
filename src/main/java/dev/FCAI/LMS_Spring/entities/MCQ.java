package dev.FCAI.LMS_Spring.entities;
import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@DiscriminatorValue("MCQ")
@Getter
@Setter
@NoArgsConstructor
public class MCQ extends Question {
    @ElementCollection
    @CollectionTable(name = "mcq_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option")
    @JsonView(Views.Summary.class)
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
