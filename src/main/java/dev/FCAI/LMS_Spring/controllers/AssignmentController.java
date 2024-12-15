package dev.FCAI.LMS_Spring.controllers;

import dev.FCAI.LMS_Spring.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    @Autowired
    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/create")
    public Assignment createAssignment(@RequestBody Assignment assignment) {
        return assignmentService.createAssignment(assignment);
    }

    @PostMapping("/submit/{assignmentId}/{userId}")
    public void submitAssignment(@PathVariable Long assignmentId, @PathVariable Long userId, @RequestParam String filePath) {
        assignmentService.submitAssignment(assignmentId, userId, filePath);
    }
}
