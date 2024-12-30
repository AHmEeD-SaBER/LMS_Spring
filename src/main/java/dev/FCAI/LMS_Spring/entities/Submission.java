package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "submission_type")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;

    @JsonView(Views.Summary.class)
    private double totalScore;

    @JsonView(Views.Detailed.class)
    private boolean isGraded;

    @ManyToOne
    @JoinColumn(name = "student_id")
    @JsonView(Views.Detailed.class)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "assessment_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Assessment assessment;
}


