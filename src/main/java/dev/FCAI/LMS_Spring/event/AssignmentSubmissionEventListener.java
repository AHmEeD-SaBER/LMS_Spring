package dev.FCAI.LMS_Spring.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AssignmentSubmissionEventListener implements ApplicationListener<AssignmentSubmissionEvent> {
    @Override
    public void onApplicationEvent(AssignmentSubmissionEvent event) {
        // Handle assignment submission logic
        // Notify instructor, start grading workflow, etc.
    }
}
