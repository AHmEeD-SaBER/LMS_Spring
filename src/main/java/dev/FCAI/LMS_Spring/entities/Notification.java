package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@JsonIdentityInfo( // Use this to resolve cycles automatically
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

    @Setter
    @Column(nullable = false)
    @JsonView(Views.Summary.class)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @JsonView(Views.Summary.class)
    private boolean isRead;

    @ManyToMany(mappedBy = "notifications")
    @JsonView(Views.Detailed.class) // No need for @JsonBackReference anymore
    private List<Student> students = new java.util.ArrayList<>();
}