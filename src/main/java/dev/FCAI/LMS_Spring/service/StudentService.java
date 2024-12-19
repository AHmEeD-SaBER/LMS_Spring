package dev.FCAI.LMS_Spring.service;
import dev.FCAI.LMS_Spring.repository.*;
import dev.FCAI.LMS_Spring.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private NotificatiosService notificationPublisher;

    public List<Course> getEnrolledCourses(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getEnrolledCourses();
    }

    @Transactional
    public boolean enrollInCourse(Long courseId, Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        // Add the course to the student's enrolled courses
        student.getEnrolledCourses().add(course);
        studentRepository.save(student);

        // Use the NotificationPublisher to send the notification
        notificationPublisher.notifyStudent(student, "You have successfully enrolled in course: " + course.getTitle());

        return true;
    }


    public List<Assessment> getSubmittedAssessments(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getSubmittedAssessments();
    }


    public List<Notification> getNotifications(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getNotifications();
    }
}

