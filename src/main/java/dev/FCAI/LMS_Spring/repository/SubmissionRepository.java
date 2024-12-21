package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
}