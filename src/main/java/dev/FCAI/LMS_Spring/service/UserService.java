package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.User;
import dev.FCAI.LMS_Spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    public User registerUser(User user) {
        return repo.save(user);
    }
}
