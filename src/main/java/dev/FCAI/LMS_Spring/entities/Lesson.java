package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String otpCode;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ElementCollection
    @CollectionTable(name = "lesson_attendance", joinColumns = @JoinColumn(name = "lesson_id"))
    @MapKeyJoinColumn(name = "student_id")
    @Column(name = "attended", nullable = false)
    private Map<Long, Boolean> attendance = new HashMap<>();

}
