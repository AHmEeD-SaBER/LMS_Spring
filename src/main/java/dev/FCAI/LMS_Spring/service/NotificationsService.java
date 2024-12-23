package dev.FCAI.LMS_Spring.service;
import dev.FCAI.LMS_Spring.entities.Instructor;
import dev.FCAI.LMS_Spring.entities.Notification;
import dev.FCAI.LMS_Spring.entities.Student;
import dev.FCAI.LMS_Spring.repository.InstructorRepository;
import dev.FCAI.LMS_Spring.repository.NotificationRepository;
import dev.FCAI.LMS_Spring.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class NotificationsService {

    @Autowired
   NotificationRepository notificationRepository;

    @Autowired
   StudentRepository studentRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Transactional
    public void notifyStudents(List<Student> students, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.getStudents().addAll(students);

        notificationRepository.save(notification);

        // Add the notification to each student and update their notifications
        students.forEach(student -> {
            student.getNotifications().add(notification);
            studentRepository.save(student);
        });
    }


    @Transactional
    public void notifyInstructor(Instructor instructor, String message) {
        notifyInstructors(List.of(instructor), message);
    }

    @Transactional
    public void notifyInstructors(List<Instructor> instructors, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.getInstructors().addAll(instructors);

        notificationRepository.save(notification);

        // Add the notification to each student and update their notifications
        instructors.forEach(student -> {
            student.getNotifications().add(notification);
            instructorRepository.save(student);
        });
    }


    @Transactional
    public void notifyStudent(Student student, String message) {
        notifyStudents(List.of(student), message);
    }
}
