package dev.FCAI.LMS_Spring.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import dev.FCAI.LMS_Spring.entities.Assessment;
import dev.FCAI.LMS_Spring.entities.Course;
import dev.FCAI.LMS_Spring.entities.Lesson;
import dev.FCAI.LMS_Spring.entities.Student;
import dev.FCAI.LMS_Spring.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/instructor")
public class InstructorController {
    @Autowired
    private InstructorService instructorService;

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

    @GetMapping("/assessments/{courseId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Assessment>> viewAllAssessmentsGrades(@PathVariable Long courseId) {
        return ResponseEntity.ok(instructorService.viewAllAssessmentsGrades(courseId));
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


    @PostMapping("/lesson/{lessonId}/attendance/{studentId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Boolean> markStudentAttendance(@PathVariable Long lessonId, @PathVariable Long studentId) {
        return ResponseEntity.ok(instructorService.markStudentAttendance(lessonId, studentId));
    }
}

