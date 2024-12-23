package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Lob
    @Column(nullable = false)
    private byte[] data;

    @ManyToOne
    @JoinColumn(name = "submission_id", nullable = false)
    private AssignmentSubmission submission;
}