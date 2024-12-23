package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmission extends BaseSubmission {

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmissionFile> submissionFiles = new ArrayList<>();
}