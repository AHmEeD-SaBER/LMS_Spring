package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    // Custom query methods can be added here
}
