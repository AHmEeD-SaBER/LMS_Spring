package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.Admin;
import dev.FCAI.LMS_Spring.entities.Instructor;
import dev.FCAI.LMS_Spring.entities.Student;
import dev.FCAI.LMS_Spring.entities.User;
import dev.FCAI.LMS_Spring.repository.AdminRepository;
import dev.FCAI.LMS_Spring.repository.InstructorRepository;
import dev.FCAI.LMS_Spring.repository.StudentRepository;
import dev.FCAI.LMS_Spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Transactional
    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null.");
        }

//        if (user.getRole() == null || user.getRole().isEmpty()) {
//            throw new IllegalArgumentException("Role is required and cannot be null or empty.");
//        }

        System.out.println("Creating user: Role=" + user.getRole()); // Debug log

        if (user instanceof Student student) {
            return studentRepository.save(student);
        } else if (user instanceof Instructor instructor) {
            return instructorRepository.save(instructor);
        } else if (user instanceof Admin admin) {
            return adminRepository.save(admin);
        }

        throw new RuntimeException("Invalid user type.");
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        studentRepository.deleteById(userId);
        instructorRepository.deleteById(userId);
    }

    @Transactional
    public User updateUser(User user) {
        if (user instanceof Student student) {
            return studentRepository.save(student);
        } else if (user instanceof Instructor instructor) {
            return instructorRepository.save(instructor);
        }
        return userRepository.save(user);
    }

    public List<User> findAllUsers(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        return admin.getUsers();
    }
}