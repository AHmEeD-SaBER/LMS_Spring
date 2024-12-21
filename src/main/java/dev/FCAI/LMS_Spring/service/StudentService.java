package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private NotificationsService notificationService;
    @Autowired
    private SubmissionRepository submissionRepository;

    public List<Course> getEnrolledCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getEnrolledCourses();
    }

    @Transactional
    public void enrollInCourse(Long courseId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        student.getEnrolledCourses().add(course);
        course.getEnrolledStudents().add(student);

        notificationService.notifyStudent(student,
                "You have successfully enrolled in course: " + course.getTitle());

        studentRepository.save(student);
        courseRepository.save(course);
    }

    public List<Notification> getNotifications(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getNotifications();
    }

    public Submission viewSubmission(Long submissionId, Long studentId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        if (!submission.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Not authorized to view this submission");
        }
        if (!submission.isGraded()) {
            throw new RuntimeException("Submission is not yet graded");
        }
        return submission;
    }
}