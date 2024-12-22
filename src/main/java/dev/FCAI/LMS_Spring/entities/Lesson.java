package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Setter
@Getter
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(Views.Summary.class)
    private String title;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonView(Views.Detailed.class)
    private Course course;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LessonMaterial> materials = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "student_lesson",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonView(Views.Detailed.class)
    private List<Student> attendedStudents = new ArrayList<>();

}