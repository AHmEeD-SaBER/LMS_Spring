package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.*;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Instructor.class, name = "QUIZ"),
        @JsonSubTypes.Type(value = Instructor.class, name = "ASSIGNMENT"),
})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "assessment_type")
public abstract class Assessment {
    @Column(name="assessment_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;


    @Column(name="title",nullable = false)
    @JsonView(Views.Summary.class)
    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id",nullable = false)
    @JsonView(Views.Summary.class)
    private Course course;

   @ManyToMany(mappedBy = "submittedAssessments", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
   private List <Student> studentAssessment=new ArrayList<>();

    private Double grade;
    @Transient
    @JsonProperty("type")
    public String getType() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

}
