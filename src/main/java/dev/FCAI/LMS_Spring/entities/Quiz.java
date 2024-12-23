package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Builder
@DiscriminatorValue("QUIZ")
@AllArgsConstructor
@NoArgsConstructor
public class Quiz extends Assessment {
    @Column(nullable = false)
    private int numQuestions;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questionBank = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<StudentQuiz> studentQuizzes = new ArrayList<>();
}
