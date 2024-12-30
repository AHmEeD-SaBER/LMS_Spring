package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DiscriminatorValue("ASSIGNMENT_SUBMISSION")
public class AssignmentSubmission extends Submission {
    @Column(columnDefinition = "TEXT")
    private String feedback;

    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubmissionFile> submissionFiles = new ArrayList<>();
}
