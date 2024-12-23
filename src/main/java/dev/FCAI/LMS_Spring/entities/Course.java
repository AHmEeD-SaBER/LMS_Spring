package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.*;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(Views.Summary.class)
    private String title;

    @Column(columnDefinition = "TEXT")
    @JsonView(Views.Summary.class)
    private String description;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    @JsonView(Views.Detailed.class)
    private Instructor instructor;

    @ManyToMany(mappedBy = "enrolledCourses")
    @JsonView(Views.Detailed.class)
    private List<Student> enrolledStudents = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonView(Views.Detailed.class)
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonView(Views.Detailed.class)
    private List<Assessment> assessments = new ArrayList<>();
}