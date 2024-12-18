//package dev.FCAI.LMS_Spring;
//
//import dev.FCAI.LMS_Spring.entities.Admin;
//import dev.FCAI.LMS_Spring.entities.Student;
//import dev.FCAI.LMS_Spring.entities.Instructor;
//import dev.FCAI.LMS_Spring.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataInitializer implements CommandLineRunner {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (userRepository.count() == 0) {
//            // Create an admin
//            Admin admin = new Admin();
//            admin.setUsername("admin");
//            admin.setPassword("123"); // `{noop}` disables password encoding for testing
//            admin.setEmail("admin@example.com");
//            admin.setFirstName("System");
//            admin.setLastName("Admin");
//            admin.setRole("ADMIN");
//            userRepository.save(admin);
//
//            // Create a student
//            Student student = new Student();
//            student.setUsername("student");
//            student.setPassword("123");
//            student.setEmail("student@example.com");
//            student.setFirstName("John");
//            student.setLastName("Doe");
//            student.setRole("STUDENT");
//            student.setAdmin(admin);  // Associate admin here
//            userRepository.save(student);
//
//            // Create an instructor
//            Instructor instructor = new Instructor();
//            instructor.setUsername("instructor");
//            instructor.setPassword("123");
//            instructor.setEmail("instructor@example.com");
//            instructor.setFirstName("Jane");
//            instructor.setLastName("Smith");
//            instructor.setRole("INSTRUCTOR");
//            instructor.setAdmin(admin); // Associate admin here
//            userRepository.save(instructor);
//
//            // Add users to admin
//            admin.getUsers().clear(); // Clear any existing users before adding new ones
//            admin.getUsers().add(student);
//            admin.getUsers().add(instructor);
//
//            userRepository.save(admin); // Save admin with associated users
//
//            System.out.println("Default users created.");
//        }
//    }
//}
