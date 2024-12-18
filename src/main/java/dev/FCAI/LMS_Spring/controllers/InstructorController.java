//package dev.FCAI.LMS_Spring.controllers;
//
//import dev.FCAI.LMS_Spring.entities.Assessment;
//import dev.FCAI.LMS_Spring.entities.Course;
//import dev.FCAI.LMS_Spring.service.InstructorService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/instructor")
//public class InstructorController {
//    @Autowired
//    private InstructorService instructorService;
//
//    @PostMapping("/course")
//    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
//        return ResponseEntity.ok(instructorService.createCourse(course));
//    }
//
//    @DeleteMapping("/course/{courseId}")
//    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
//        instructorService.deleteCourse(courseId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/course")
//    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
//        return ResponseEntity.ok(instructorService.updateCourse(course));
//    }
//
//    @PostMapping("/assessment")
//    public ResponseEntity<Assessment> createAssessment(@RequestBody Assessment assessment) {
//        return ResponseEntity.ok(instructorService.createAssessment(assessment));
//    }
//
//    @GetMapping("/assessments/{courseId}")
//    public ResponseEntity<List<Assessment>> viewAllAssessmentsGrades(@PathVariable Long courseId) {
//        return ResponseEntity.ok(instructorService.viewAllAssessmentsGrades(courseId));
//    }
//}
