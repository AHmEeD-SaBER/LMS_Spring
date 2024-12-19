package dev.FCAI.LMS_Spring.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import dev.FCAI.LMS_Spring.service.*;
import dev.FCAI.LMS_Spring.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.lang.Long;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/courses/{id}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Course>> getEnrolledCourses(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getEnrolledCourses(id));
    }

    @PostMapping("/enroll/{courseId}/{id}")
    public ResponseEntity<Boolean> enrollInCourse(@PathVariable Long courseId, @PathVariable Long id) {
        boolean enrolled = studentService.enrollInCourse(courseId,id);
        return ResponseEntity.ok(enrolled);
    }

    @GetMapping("/grades/{id}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Assessment>> getSubmittedAssessmentGrades(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getSubmittedAssessments(id));
    }

    @GetMapping("/notifications/{id}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getNotifications(id));
    }

    @PostMapping("/submit/{aId}/{id}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Assessment> submitAssessment(@PathVariable Long aId, @PathVariable Long id) {
        return ResponseEntity.ok(studentService.submitAssessment(aId,id));
    }
}

