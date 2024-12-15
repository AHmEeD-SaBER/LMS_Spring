package dev.FCAI.LMS_Spring.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

// Event Handlers
@Component
public class UserRegistrationEventListener implements ApplicationListener<UserRegistrationEvent> {
    @Override
    public void onApplicationEvent(UserRegistrationEvent event) {
        // Handle user registration logic
        // Send welcome email, create initial notifications, etc.
    }
}
