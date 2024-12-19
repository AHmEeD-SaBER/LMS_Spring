package dev.FCAI.LMS_Spring.service;
import dev.FCAI.LMS_Spring.entities.Notification;
import dev.FCAI.LMS_Spring.entities.Student;
import dev.FCAI.LMS_Spring.repository.NotificationRepository;
import dev.FCAI.LMS_Spring.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class NotificatiosService {

    private final NotificationRepository notificationRepository;
    private final StudentRepository studentRepository;

    public NotificatiosService(NotificationRepository notificationRepository, StudentRepository studentRepository) {
        this.notificationRepository = notificationRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Create and save notifications for a list of students.
     * This method can be reused to generate notifications for any action/event.
     *
     * @param students List of students to notify
     * @param message  The message to send
     */
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

    /**
     * Notify a single student.
     *
     * @param student The student to notify
     * @param message The message to send
     */
    @Transactional
    public void notifyStudent(Student student, String message) {
        notifyStudents(List.of(student), message);
    }
}
