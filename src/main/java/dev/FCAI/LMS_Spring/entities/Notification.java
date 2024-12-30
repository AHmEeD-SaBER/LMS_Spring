package dev.FCAI.LMS_Spring.entities;
import com.fasterxml.jackson.annotation.*;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;
    @Column(nullable = false)
    @JsonView(Views.Summary.class)
    private String message;
    @Column(nullable = false)
    @JsonView(Views.Summary.class)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    @JsonView(Views.Summary.class)
    private boolean isRead;
    @ManyToMany
    @JoinTable(
            name = "student_notification",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonView(Views.Detailed.class)
    private List<Student> students = new ArrayList<>();
    @ManyToMany
    @JoinTable(
            name = "instructor_notification",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "instructor_id")
    )
    @JsonView(Views.Detailed.class)
    private List<Instructor> instructors = new ArrayList<>();
}