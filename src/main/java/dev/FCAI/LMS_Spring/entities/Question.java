package dev.FCAI.LMS_Spring.entities;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "question_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MCQ.class, name = "MCQ"),
        @JsonSubTypes.Type(value = TrueFalse.class, name = "TrueFalse"),
        @JsonSubTypes.Type(value = shortAnswer.class, name = "shortAnswer"),
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "question_type")
@Getter
@Setter
public abstract class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView(Views.Summary.class)
    protected double grade;

    @Column(nullable = false)
    @JsonView(Views.Detailed.class)
    protected String correctAnswer;

    @Column(nullable = false)
    @JsonView(Views.Summary.class)
    protected String questionText;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    @JsonView(Views.Detailed.class)
    private Assessment assessment;

    public abstract double gradeQuestion(String submittedAnswer);
}


