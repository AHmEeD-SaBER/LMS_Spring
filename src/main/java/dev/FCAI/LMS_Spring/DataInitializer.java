package dev.FCAI.LMS_Spring;

import dev.FCAI.LMS_Spring.entities.Admin;
import dev.FCAI.LMS_Spring.entities.Student;
import dev.FCAI.LMS_Spring.entities.Instructor;
import dev.FCAI.LMS_Spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // Create an admin
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("123"); // `{noop}` disables password encoding for testing
            admin.setEmail("admin@example.com");
            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);

            // Create a student
            Student student = new Student();
            student.setUsername("student");
            student.setPassword("123");
            student.setEmail("student@example.com");
            student.setFirstName("John");
            student.setLastName("Doe");
            student.setRole("ROLE_STUDENT");
            userRepository.save(student);

            // Create an instructor
            Instructor instructor = new Instructor();
            instructor.setUsername("instructor");
            instructor.setPassword("123");
            instructor.setEmail("instructor@example.com");
            instructor.setFirstName("Jane");
            instructor.setLastName("Smith");
            instructor.setRole("ROLE_INSTRUCTOR");
            userRepository.save(instructor);

            System.out.println("Default users created.");
        }
    }
}
