package dev.FCAI.LMS_Spring.entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "question_type")
@Getter
@Setter
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected double grade;

    @Column(nullable = false)
    protected String correctAnswer;

    @Column(nullable = false)
    protected String questionText;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    private Assessment assessment;

    public abstract double gradeQuestion(String submittedAnswer);
}


