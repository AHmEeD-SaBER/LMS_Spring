package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.repository.AssignmentRepository;
import dev.FCAI.LMS_Spring.event.AssignmentSubmissionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public AssignmentService(AssignmentRepository assignmentRepository, ApplicationEventPublisher eventPublisher) {
        this.assignmentRepository = assignmentRepository;
        this.eventPublisher = eventPublisher;
    }

    public Assignment createAssignment(Assignment assignment) {
        Assignment createdAssignment = assignmentRepository.save(assignment);
        return createdAssignment;
    }

    public void submitAssignment(Long assignmentId, Long userId, String filePath) {
        // Handle submission logic
        AssignmentSubmissionEvent event = new AssignmentSubmissionEvent(this, filePath);
        eventPublisher.publishEvent(event);
    }
}
