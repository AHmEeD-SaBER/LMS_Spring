package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private NotificationsService notificationPublisher;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public Course createCourse(Course course, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        instructor.getCreatedCourses().add(course);
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    @Transactional
    public Assessment createAssessment(Assessment assessment, Long courseId, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!instructor.getCreatedCourses().contains(course)) {
            throw new RuntimeException("Instructor does not own the course");
        }

        assessment.setCourse(course);
        course.getAssessments().add(assessment);

        List<Student> enrolledStudents = course.getEnrolledStudents();
        for (Student student : enrolledStudents) {
            notificationPublisher.notifyStudent(student,
                    "New Assessment Published: " + course.getTitle() + " - " + assessment.getTitle());
        }

        courseRepository.save(course);
        return assessmentRepository.save(assessment);
    }

    @Transactional
    public void gradeEssayQuestion(Long submissionId, Long questionId, double awardedScore) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        SubmittedAnswer submittedAnswer = submission.getSubmittedAnswers().stream()
                .filter(sa -> sa.getQuestion().getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Question not found in submission"));

        submittedAnswer.setAwardedScore(awardedScore);

        double totalScore = submission.getSubmittedAnswers().stream()
                .mapToDouble(SubmittedAnswer::getAwardedScore)
                .sum();
        submission.setTotalScore(totalScore);
        boolean allGraded = submission.getSubmittedAnswers().stream()
                .filter(sa -> sa.getQuestion() instanceof shortAnswer)
                .allMatch(sa -> sa.getAwardedScore() > 0);

        if (allGraded) {
            submission.setGraded(true);
            Student student = submission.getStudent();
            notificationPublisher.notifyStudent(student,
                    "Your grades for " + submission.getAssessment().getTitle() + " are now available.");
        }

        submissionRepository.save(submission);
    }

    @Transactional
    public Lesson createLesson(Lesson lesson, Long courseId, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!instructor.getCreatedCourses().contains(course)) {
            throw new RuntimeException("Instructor does not own this course");
        }

        lesson.setCourse(course);
        course.getLessons().add(lesson);

        List<Student> enrolledStudents = course.getEnrolledStudents();
        for (Student student : enrolledStudents) {
            notificationPublisher.notifyStudent(student,
                    "New Lesson Added: " + course.getTitle() + " - " + lesson.getTitle());
        }

        lessonRepository.save(lesson);
        return lesson;
    }
}