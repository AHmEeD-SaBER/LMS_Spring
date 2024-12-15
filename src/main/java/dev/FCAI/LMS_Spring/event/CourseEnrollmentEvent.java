package dev.FCAI.LMS_Spring.event;

public class CourseEnrollmentEvent extends LmsEvent {
    public CourseEnrollmentEvent(Object source, Object enrollmentData) {
        super(source, "COURSE_ENROLLMENT", enrollmentData);
    }
}
