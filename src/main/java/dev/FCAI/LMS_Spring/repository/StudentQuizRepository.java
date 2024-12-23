package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.Student;
import dev.FCAI.LMS_Spring.entities.StudentQuiz;
import dev.FCAI.LMS_Spring.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentQuizRepository extends JpaRepository<StudentQuiz, Long> {
    Optional<StudentQuiz> findByStudentAndQuiz(Student student, Quiz quiz);
}