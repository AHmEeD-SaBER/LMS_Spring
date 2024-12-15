package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
