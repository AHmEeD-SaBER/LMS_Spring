// InstructorController.java
package dev.FCAI.LMS_Spring.controllers;

import dev.FCAI.LMS_Spring.dto.request.*;
import dev.FCAI.LMS_Spring.dto.response.*;
import dev.FCAI.LMS_Spring.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private InstructorService instructorService;

    @PostMapping("/course")
    public ResponseEntity<CourseResponseDTO> createCourse(@RequestBody CourseRequestDTO courseRequestDTO) {
        CourseResponseDTO courseResponse = instructorService.createCourse(courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(courseResponse);
    }

    @PutMapping("/course/{courseId}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable Long courseId, @RequestBody CourseRequestDTO courseRequestDTO) {
        CourseResponseDTO courseResponse = instructorService.updateCourse(courseId, courseRequestDTO);
        return ResponseEntity.ok(courseResponse);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<CourseResponseDTO> getCourse(@PathVariable Long courseId) {
        CourseResponseDTO courseResponse = instructorService.getCourse(courseId);
        return ResponseEntity.ok(courseResponse);
    }

    @GetMapping("/courses/{instructorId}")
    public ResponseEntity<List<CourseResponseDTO>> getCoursesByInstructor(@PathVariable Long instructorId) {
        List<CourseResponseDTO> courses = instructorService.getCoursesByInstructor(instructorId);
        return ResponseEntity.ok(courses);
    }

    @DeleteMapping("/course/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long courseId) {
        instructorService.deleteCourse(courseId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lesson")
    public ResponseEntity<LessonResponseDTO> createLesson(@RequestBody LessonRequestDTO lessonRequestDTO) {
        LessonResponseDTO lessonResponse = instructorService.createLesson(lessonRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonResponse);
    }

    @PostMapping("/assessment")
    public ResponseEntity<AssessmentResponseDTO> createAssessment(@RequestBody AssessmentRequestDTO assessmentDTO) {
        AssessmentResponseDTO assessmentResponse = instructorService.createAssessment(assessmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(assessmentResponse);
    }

    @PostMapping("/quiz/{quizId}/question")
    public ResponseEntity<QuestionResponseDTO> addQuestionToQuiz(@PathVariable Long quizId, @RequestBody QuestionRequestDTO questionDTO) {
        QuestionResponseDTO questionResponse = instructorService.addQuestionToQuiz(quizId, questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(questionResponse);
    }

}