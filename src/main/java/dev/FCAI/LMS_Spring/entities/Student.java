package dev.FCAI.LMS_Spring.entities;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@DiscriminatorValue("STUDENT")
public class Student extends User {
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonView(Views.Detailed.class)
    private List<Course> enrolledCourses = new java.util.ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "student_notifications",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id")
    )
    @JsonView(Views.Detailed.class)
    private List<Notification> notifications = new java.util.ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "lesson_attendance",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id")
    )
    private List<Lesson> attendedLessons = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentSubmission> assignmentSubmissions = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentQuiz> studentQuizzes = new ArrayList<>();
}