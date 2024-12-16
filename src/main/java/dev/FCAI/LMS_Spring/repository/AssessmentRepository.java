package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    // Assessment-specific repository methods
}
