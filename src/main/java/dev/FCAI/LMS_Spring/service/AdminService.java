package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.Admin;
import dev.FCAI.LMS_Spring.entities.Student;
import dev.FCAI.LMS_Spring.entities.User;
import dev.FCAI.LMS_Spring.repository.AdminRepository;
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
    private AdminRepository adminRepository;

    @Transactional
    public User createUser(User user) {
        // If the user is a Student, we need to associate it with an Admin
        if (user instanceof Student) {
            Student student = (Student) user;

            // Retrieve the Admin associated with this Student (you can adjust this logic based on your needs)
            Admin admin = student.getAdmin();  // Assuming you pass the admin directly in the request (through adminId)
            if (admin != null) {
                // Ensure the admin exists in the DB
                Admin existingAdmin = adminRepository.findById(admin.getId())
                        .orElseThrow(() -> new RuntimeException("Admin not found"));

                // Set the admin to the student
                student.setAdmin(existingAdmin);
            } else {
                throw new RuntimeException("Admin must be provided for the student");
            }
        }

        // Save the user, which could be a Student, Admin, or other types of User
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }
//
//    public List<User> findAllusers(){
//
//    }
}
