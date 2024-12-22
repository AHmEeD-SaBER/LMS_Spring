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

    @GetMapping("/courses/{id}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Course>> getEnrolledCourses(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getEnrolledCourses(id));
    }

    @GetMapping("/course/{id}/{courseId")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Course> getEnrolledCourse(@PathVariable Long id, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.getEnrolledCourse(id, courseId));
    }

    @GetMapping("/course/lesson/{id}/{lessonId")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Lesson> getLesson(@PathVariable Long id, @PathVariable Long lessonId) {
        return ResponseEntity.ok(studentService.getLesson(id, lessonId));
    }

    @PostMapping("enroll/{studentId}/{courseId}/")
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        studentService.enrollInCourse(courseId, studentId);
        return ResponseEntity.ok("Enrolled successfully");
    }

    @GetMapping("/assessment/{id}/{assessmentId}")
    public ResponseEntity<Assessment> getAssessment(@PathVariable Long id, @PathVariable Long assessmentId){
        return ResponseEntity.ok(assessmentService.getAssessmentForStudent(id, assessmentId));
    }

    @PostMapping("/submit/{assessmentId}/{id}")
    public ResponseEntity<String> submitAssessment(
            @PathVariable Long id,
            @PathVariable Long assessmentId,
            @RequestBody Map<Long, String> answers) {
        assessmentService.submitAssessment(assessmentId, id, answers);
        return ResponseEntity.ok("Assessment submitted successfully");
    }

    @GetMapping("notifications/{id}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getNotifications(id));
    }


    @GetMapping("/submission/{submissionId}/{id}")
    @JsonView(Views.Detailed.class)
    public ResponseEntity<Submission> getSubmission(
            @PathVariable Long id,
            @PathVariable Long submissionId) {
        Submission submission = studentService.viewSubmission(submissionId, id);
        return ResponseEntity.ok(submission);
    }

    @GetMapping("/submission/{id}")
    @JsonView(Views.Detailed.class)
    public ResponseEntity<List<Submission>> getSubmissions(
            @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getSubmissions(id));
    }




}