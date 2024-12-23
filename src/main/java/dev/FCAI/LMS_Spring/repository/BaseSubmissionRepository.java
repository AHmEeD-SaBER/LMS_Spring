package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.BaseSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseSubmissionRepository extends JpaRepository<BaseSubmission, Long> {
}