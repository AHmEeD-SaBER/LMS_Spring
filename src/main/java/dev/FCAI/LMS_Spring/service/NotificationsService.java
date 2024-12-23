package dev.FCAI.LMS_Spring.service;
import dev.FCAI.LMS_Spring.entities.Instructor;
import dev.FCAI.LMS_Spring.entities.Notification;
import dev.FCAI.LMS_Spring.entities.Student;
import dev.FCAI.LMS_Spring.repository.InstructorRepository;
import dev.FCAI.LMS_Spring.repository.NotificationRepository;
import dev.FCAI.LMS_Spring.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class NotificationsService {

    private final NotificationRepository notificationRepository;
    private final InstructorRepository instructorRepository;
    private final StudentRepository studentRepository;

    public NotificationsService(NotificationRepository notificationRepository, InstructorRepository instructorRepository, StudentRepository studentRepository) {
        this.notificationRepository = notificationRepository;
        this.instructorRepository = instructorRepository;
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

    @Transactional
    public void notifyInstructors(List<Instructor> instructors, String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);
        notification.getInstructors().addAll(instructors);

        notificationRepository.save(notification);

        // Add the notification to each student and update their notifications
        instructors.forEach(instructor -> {
            instructor.getNotifications().add(notification);
            instructorRepository.save(instructor);
        });
    }


    /**
     * Notify a single instructor.
     *
     * @param instructor The student to notify
     * @param message The message to send
     */
    @Transactional
    public void notifyInstructor(Instructor instructor, String message) {
        notifyInstructors(List.of(instructor), message);
    }


}
