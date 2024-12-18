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

//    public List<Course> getEnrolledCourses() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Student student = studentRepository.findByUsername(auth.getName());
//        return student.getEnrolledCourses();
//    }

//    @Transactional
//    public boolean enrollInCourse(Long courseId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Student student = studentRepository.findByUsername(auth.getName());
//
//        Optional<Course> courseOptional = courseRepository.findById(courseId);
//        if (courseOptional.isPresent()) {
//            Course course = courseOptional.get();
//            student.getEnrolledCourses().add(course);
//            studentRepository.save(student);
//
//            // Create enrollment notification
//            Notification notification = new Notification();
//            notification.setMessage("Successfully enrolled in course: " + course.getTitle());
//            notification.setCreatedAt(LocalDateTime.now());
//            notification.setRead(false);
//            notification.getStudents().add(student);
//            notificationRepository.save(notification);
//
//            return true;
//        }
//        return false;
//    }


//    public List<Assessment> getSubmittedAssessmentGrades() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Student student = studentRepository.findByUsername(auth.getName());
//        return student.getSubmittedAssessments();
//    }


//    public List<Notification> getNotifications() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Student student = studentRepository.findByUsername(auth.getName());
//        return student.getNotifications();
//    }
}

