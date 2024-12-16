package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.User;
import dev.FCAI.LMS_Spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
