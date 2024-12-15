package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.User;
import dev.FCAI.LMS_Spring.event.UserRegistrationEvent;
import dev.FCAI.LMS_Spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    public User registerUser(User user) {
        // Save user to the database
        User savedUser = userRepository.save(user);

        // Trigger a user registration event to notify listeners
        eventPublisher.publishEvent(new UserRegistrationEvent(this, savedUser));

        return savedUser;
    }
}
