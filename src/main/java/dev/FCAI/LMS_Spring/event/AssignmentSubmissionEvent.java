package dev.FCAI.LMS_Spring.event;

public class AssignmentSubmissionEvent extends LmsEvent {
    public AssignmentSubmissionEvent(Object source, Object submissionData) {
        super(source, "ASSIGNMENT_SUBMISSION", submissionData);
    }
}
