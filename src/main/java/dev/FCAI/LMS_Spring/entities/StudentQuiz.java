package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class StudentQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Associate with Student
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    // Associate with Quiz
    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    // The questions assigned to this student for this quiz
    @ManyToMany
    @JoinTable(
            name = "student_quiz_questions",
            joinColumns = @JoinColumn(name = "student_quiz_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions = new ArrayList<>();

    // Submission
    @OneToOne(mappedBy = "studentQuiz", cascade = CascadeType.ALL)
    private QuizSubmission submission;
}