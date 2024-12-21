package dev.FCAI.LMS_Spring.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.lang.Long;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/{studentId}/courses")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Course>> getEnrolledCourses(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getEnrolledCourses(studentId));
    }

    @PostMapping("/{studentId}/courses/{courseId}/enroll")
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        studentService.enrollInCourse(courseId, studentId);
        return ResponseEntity.ok("Enrolled successfully");
    }

    @GetMapping("/{studentId}/notifications")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getNotifications(studentId));
    }


    @GetMapping("/{studentId}/submissions/{submissionId}")
    @JsonView(Views.Detailed.class)
    public ResponseEntity<Submission> getSubmission(
            @PathVariable Long studentId,
            @PathVariable Long submissionId) {
        Submission submission = studentService.viewSubmission(submissionId, studentId);
        return ResponseEntity.ok(submission);
    }


    @PostMapping("/{studentId}/assessments/{assessmentId}/submit")
    public ResponseEntity<String> submitAssessment(
            @PathVariable Long studentId,
            @PathVariable Long assessmentId,
            @RequestBody Map<Long, String> answers) {
        assessmentService.submitAssessment(assessmentId, studentId, answers);
        return ResponseEntity.ok("Assessment submitted successfully");
    }
}