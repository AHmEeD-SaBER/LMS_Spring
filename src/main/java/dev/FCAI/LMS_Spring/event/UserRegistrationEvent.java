package dev.FCAI.LMS_Spring.event;

// Specific Event Types
public class UserRegistrationEvent extends LmsEvent {
    public UserRegistrationEvent(Object source, Object userData) {
        super(source, "USER_REGISTRATION", userData);
    }
}
