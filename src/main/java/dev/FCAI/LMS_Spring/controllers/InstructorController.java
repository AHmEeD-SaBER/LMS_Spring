package dev.FCAI.LMS_Spring.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/instructor")
public class InstructorController {
    @Autowired
    private InstructorService instructorService;
    @GetMapping("/")
    public String greet() {
        return "Hello instructor";
    }
    @PostMapping("/course")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Course> createCourse(@RequestBody Course course, @RequestParam Long instructorId) {
        return ResponseEntity.ok(instructorService.createCourse(course, instructorId));
    }

    @DeleteMapping("/course/{courseId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId, @RequestParam Long instructorId) {
        instructorService.deleteCourse(courseId, instructorId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/course")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Course> updateCourse(@RequestBody Course course, @RequestParam Long instructorId) {
        return ResponseEntity.ok(instructorService.updateCourse(course, instructorId));
    }

    @PostMapping("/assessment")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Assessment> createAssessment(@RequestBody Assessment assessment, @RequestParam Long courseId, @RequestParam Long instructorId) {
        return ResponseEntity.ok(instructorService.createAssessment(assessment, courseId, instructorId));
    }

    @GetMapping("/courses")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Course>> viewAllCourses(@RequestParam Long instructorId) {
        return ResponseEntity.ok(instructorService.viewAllCourses(instructorId));
    }

    @PostMapping("/course/{courseId}/lesson/{instructorId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson, @PathVariable Long courseId, @PathVariable Long instructorId) {
        Lesson createdLesson = instructorService.createLesson(lesson, courseId, instructorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @PostMapping("/{instructorId}/lessons/{lessonId}/materials/upload")
    public ResponseEntity<String> uploadLessonMaterial(
            @PathVariable Long instructorId,
            @PathVariable Long lessonId,
            @RequestParam("file") MultipartFile file) {
        try {
            instructorService.uploadLessonMaterial(instructorId, lessonId, file);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error uploading file: " + e.getMessage());
        }
    }


    @PostMapping("/lesson/{lessonId}/attendance/{studentId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Boolean> markStudentAttendance(@PathVariable Long lessonId, @PathVariable Long studentId) {
        return ResponseEntity.ok(instructorService.markStudentAttendance(lessonId, studentId));
    }

    @PostMapping("/assessment/{submissionId}/grade")
    @JsonView(Views.Summary.class)
    public ResponseEntity<AssignmentSubmission> gradeAssessment(@PathVariable Long submissionId, @RequestParam Double grade, @RequestParam String feedback) {
        return ResponseEntity.ok(instructorService.gradeAssignment(submissionId, grade, feedback));
    }

    @GetMapping("/notifications/{instructorId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long instructorId) {
        return ResponseEntity.ok(instructorService.getNotifications(instructorId));
    }
}

