package dev.FCAI.LMS_Spring.entities;
import com.fasterxml.jackson.annotation.*;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Quiz.class, name = "QUIZ"),
        @JsonSubTypes.Type(value = Assignment.class, name = "ASSIGNMENT"),
})
@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "assessment_type")
public abstract class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assessment_id")
    @JsonView(Views.Summary.class)
    private Long id;

    @Column(name = "title", nullable = false)
    @JsonView(Views.Summary.class)
    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonView(Views.Detailed.class)
    private Course course;

    @Column(name = "grade", nullable = false)
    @JsonView(Views.Summary.class)
    private Double grade;

    // New relationship to questions
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Views.Summary.class)
    private List<Question> questions = new ArrayList<>();

    // Relationship to submissions
    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Submission> submissions = new ArrayList<>();

    @Transient
    @JsonProperty("type")
    public String getType() {
        DiscriminatorValue val = this.getClass().getAnnotation(DiscriminatorValue.class);
        if (val != null) {
            return val.value();
        } else {
            return null;
        }
    }
}