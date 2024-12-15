package dev.FCAI.LMS_Spring.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CourseEnrollmentEventListener implements ApplicationListener<CourseEnrollmentEvent> {
    @Override
    public void onApplicationEvent(CourseEnrollmentEvent event) {
        // Handle course enrollment logic
        // Send confirmation, update student records, etc.
    }
}
