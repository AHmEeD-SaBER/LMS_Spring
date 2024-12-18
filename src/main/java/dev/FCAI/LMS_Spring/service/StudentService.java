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
    private NotificationRepository notificationRepository;

    public List<Course> getEnrolledCourses(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getEnrolledCourses();
    }

    @Transactional
    public boolean enrollInCourse(Long courseId, Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            student.getEnrolledCourses().add(course);
            studentRepository.save(student);
            Notification notification = new Notification();
            notification.setMessage("Successfully enrolled in course: " + course.getTitle());
            notification.setCreatedAt(LocalDateTime.now());
            notification.setRead(false);
            notification.getStudents().add(student);
            notificationRepository.save(notification);
            return true;
        }
        return false;
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

