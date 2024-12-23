package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSubmission extends BaseSubmission {

    @OneToOne
    @JoinColumn(name = "student_quiz_id", nullable = false)
    private StudentQuiz studentQuiz;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmittedAnswer> submittedAnswers = new ArrayList<>();
}