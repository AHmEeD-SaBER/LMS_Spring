package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    // Quiz-specific repository methods
}
