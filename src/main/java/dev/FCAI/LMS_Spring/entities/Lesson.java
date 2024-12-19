package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Column(nullable = false)
    @JsonView(Views.Summary.class)
    private String otpCode;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference("course-lessons")
    @JsonView(Views.Detailed.class)
    private Course course;

    @ManyToMany(mappedBy = "attendedLessons")
    @JsonManagedReference("lesson-attendance")
    @JsonView(Views.Detailed.class)
    private List<Student> attendedStudents = new ArrayList<>();


}
