package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    // Lesson-specific repository methods
}
