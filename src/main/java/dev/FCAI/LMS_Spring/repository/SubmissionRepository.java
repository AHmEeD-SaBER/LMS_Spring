package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.entities.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudentAndAssessment_Course(Student student, Course course);
    List<Submission> findByStudentAndAssessment(Student student, Assessment assessment);
    List<Submission> findByAssessment(Assessment assessment);
    List<Submission> findByAssessment_Course(Course course);

    @Query("SELECT s FROM Submission s WHERE s.assessment.course = :course " +
            "AND TYPE(s) = :submissionType")
    List<Submission> findByCourseAndType(Course course, String submissionType);
}