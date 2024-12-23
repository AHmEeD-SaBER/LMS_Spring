package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@DiscriminatorValue("ASSIGNMENT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment extends Assessment {
    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "submission_instructions", length = 2000)
    private String submissionInstructions;

    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssignmentSubmission> submissions = new ArrayList<>();
}