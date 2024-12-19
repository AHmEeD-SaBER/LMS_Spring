package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.*;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Setter
@Getter
@Entity
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

    @ManyToOne(cascade = CascadeType.ALL)

    @JoinColumn(name = "instructor_id")
    @JsonView(Views.Detailed.class)
    @JsonBackReference("instructor-courses")
    private Instructor instructor;

    @ManyToMany(mappedBy = "enrolledCourses", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonView(Views.Detailed.class)
    @JsonManagedReference("course-students")
    private List<Student> enrolledStudents = new java.util.ArrayList<>();


    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonView(Views.Detailed.class)
    @JsonManagedReference("course-lessons")
    private List<Lesson> lessons = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonView(Views.Detailed.class)
    @JsonManagedReference("course-assessments")
    private List<Assessment> assessments = new java.util.ArrayList<>();

}
