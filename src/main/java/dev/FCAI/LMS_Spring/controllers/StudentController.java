package dev.FCAI.LMS_Spring.controllers;

import dev.FCAI.LMS_Spring.service.*;
import dev.FCAI.LMS_Spring.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired

    @GetMapping("/courses/{id}")
    public ResponseEntity<List<Course>> getEnrolledCourses(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getEnrolledCourses(id));
    }

    @PostMapping("/enroll/{courseId}/{id}")
    public ResponseEntity<Boolean> enrollInCourse(@PathVariable Long courseId, @PathVariable Long id) {
        boolean enrolled = studentService.enrollInCourse(courseId,id);
        return ResponseEntity.ok(enrolled);
    }

    @GetMapping("/grades/{id}")
    public ResponseEntity<List<Assessment>> getSubmittedAssessmentGrades(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getSubmittedAssessments(id));
    }

    @GetMapping("/notifications/{id}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getNotifications(id));
    }

    @PostMapping("/submit/{aId}/{id}")
    public ResponseEntity<Assessment> submitAssessment(@PathVariable Long aId, @PathVariable Long id) {
        return ResponseEntity.ok(studentService.submitAssessment(aId,id));
    }
}

