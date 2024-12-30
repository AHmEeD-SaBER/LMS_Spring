package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.Admin;
import dev.FCAI.LMS_Spring.entities.User;
import dev.FCAI.LMS_Spring.repository.AdminRepository;
import dev.FCAI.LMS_Spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminRepository adminRepository;

    public boolean hasUsers() {
        return userRepository.count() > 0;
    }

    public User createFirstAdmin(User user) {
        if (hasUsers()) {
            throw new IllegalStateException("Cannot create first admin: Users already exist");
        }
        // Additional validation and processing
        return userRepository.save(user);
    }

    @Transactional
    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null.");
        }

        if (user.getRole() == null || user.getRole().isEmpty()) {
            throw new IllegalArgumentException("Role is required and cannot be null or empty.");
        }
        Admin admin = user.getAdmin();
        if (admin == null || admin.getId() == null) {
            throw new IllegalArgumentException("Admin must be provided and cannot be null.");
        }

        Admin existingAdmin = adminRepository.findById(admin.getId())
                .orElseThrow(() -> new IllegalArgumentException("Admin with id " + admin.getId() + " does not exist."));
        if (existingAdmin.getUsers().contains(user)) {throw new IllegalArgumentException("User already exists.");}
        existingAdmin.getUsers().add(user);
        adminRepository.save(existingAdmin);

        user.setAdmin(existingAdmin);

        System.out.println("Creating user: Role=" + user.getRole());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        }
        else{
            throw new IllegalArgumentException("User not found.");
        }
    }

    @Transactional
    public User updateUser(User user) {
        if (userRepository.findById(user.getId()).isPresent()) {
            return userRepository.save(user);
        }
        else{
            throw new IllegalArgumentException("User not found");
        }
    }

    public List<User> findAllUsers(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        return admin.getUsers();
    }
}