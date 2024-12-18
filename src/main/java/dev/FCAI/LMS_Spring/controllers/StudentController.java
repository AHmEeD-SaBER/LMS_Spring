//package dev.FCAI.LMS_Spring.controllers;
//
//import dev.FCAI.LMS_Spring.service.*;
//import dev.FCAI.LMS_Spring.entities.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/student")
//public class StudentController {
//    @Autowired
//    private StudentService studentService;
//
//    @GetMapping("/courses")
//    public ResponseEntity<List<Course>> getEnrolledCourses() {
//        return ResponseEntity.ok(studentService.getEnrolledCourses());
//    }
//
//    @PostMapping("/enroll/{courseId}")
//    public ResponseEntity<Boolean> enrollInCourse(@PathVariable Long courseId) {
//        boolean enrolled = studentService.enrollInCourse(courseId);
//        return ResponseEntity.ok(enrolled);
//    }
//
//    @GetMapping("/grades")
//    public ResponseEntity<List<Assessment>> getSubmittedAssessmentGrades() {
//        return ResponseEntity.ok(studentService.getSubmittedAssessmentGrades());
//    }
//
//    @GetMapping("/notifications")
//    public ResponseEntity<List<Notification>> getNotifications() {
//        return ResponseEntity.ok(studentService.getNotifications());
//    }
//}
//
