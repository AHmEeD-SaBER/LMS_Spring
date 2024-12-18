package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.action.internal.OrphanRemovalAction;

import java.util.List;

@Setter
@Getter
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @ManyToMany(mappedBy = "enrolledCourses", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonBackReference
    private List<Student> enrolledStudents = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Assessment> assessments = new java.util.ArrayList<>();

}
