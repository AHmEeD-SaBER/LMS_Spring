package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    // Assignment-specific repository methods
}
