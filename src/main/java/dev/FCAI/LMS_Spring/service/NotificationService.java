package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class NotificationService {

    private final JavaMailSender mailSender;

    @Autowired
    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(User user) {
        String subject = "Welcome to the LMS!";
        String content = "Hello " + user.getUsername() + ",\n\nWelcome to the Learning Management System.";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("no-reply@lms.com");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendCourseEnrollmentNotification(User user, String courseTitle) {
        String subject = "Course Enrollment Confirmation";
        String content = "Hello " + user.getUsername() + ",\n\nYou have been successfully enrolled in the course: " + courseTitle;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("no-reply@lms.com");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
