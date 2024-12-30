package dev.FCAI.LMS_Spring.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.CourseRepository;
import dev.FCAI.LMS_Spring.repository.InstructorRepository;
import dev.FCAI.LMS_Spring.repository.StudentRepository;
import dev.FCAI.LMS_Spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.lang.Long;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private NotificationsService notificationService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/")
    public String greet() {
        return "Hello student";
    }
    @GetMapping("/courses/{id}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Course>> getEnrolledCourses(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getEnrolledCourses(id));
    }

    @GetMapping("/course/{id}/{courseId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Course> getEnrolledCourse(@PathVariable Long id, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.getEnrolledCourse(id, courseId));
    }

    @GetMapping("/course/lesson/{id}/{lessonId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Lesson> getLesson(@PathVariable Long id, @PathVariable Long lessonId) {
        return ResponseEntity.ok(studentService.getLesson(lessonId, id));
    }

    @GetMapping("/lessons/{lessonId}/materials/{materialId}/download")
    @JsonView(Views.Summary.class)
    public ResponseEntity<byte[]> downloadLessonMaterial(
            @PathVariable Long lessonId,
            @PathVariable Long materialId) {
        try {
            LessonMaterial lessonMaterial = studentService.getLessonMaterial(lessonId, materialId);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + lessonMaterial.getFilename() + "\"")
                    .body(lessonMaterial.getData());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("enroll/{studentId}/{courseId}/")
    @JsonView(Views.Summary.class)
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId, @PathVariable Long studentId) {
        studentService.enrollInCourse(courseId, studentId);;
        Student student = studentRepository.findById(studentId).get();
        Course course = courseRepository.findById(courseId).get();
        notificationService.notifyStudent(student, "You have been enrolled in the course " + course.getTitle());
        notificationService.notifyInstructor(course.getInstructor(), "Student " + student.getFirstName() + " " + student.getLastName() + " has been enrolled in your course " + course.getTitle());
        return ResponseEntity.ok("Enrolled successfully");
    }

    @GetMapping("/assessment/{id}/{quizId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Question>> startQuiz(@PathVariable Long id, @PathVariable Long quizId){
        return ResponseEntity.ok(studentService.startQuiz(quizId, id));
    }

    @PostMapping("/submitQuiz/{submissionId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Boolean> submitQuiz(
            @PathVariable Long submissionId,
            @RequestBody Map<Long, String> answers) {
        return ResponseEntity.ok(studentService.submitQuiz(submissionId, answers));
    }

    @PostMapping("/submitAssignment/{studentId}/{assignmentId}")
    public ResponseEntity<String> submitAssignment(
            @PathVariable Long studentId,
            @PathVariable Long assignmentId,
            @RequestParam("files") List<MultipartFile> files) {
        studentService.submitAssignment(assignmentId, studentId, files);
        return ResponseEntity.ok("Assignment submitted successfully.");
    }

    @GetMapping("notifications/{id}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getNotifications(id));
    }

    @GetMapping("/grades/course/{studentId}/{courseId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<Submission>> getStudentCourseGrades(@PathVariable Long studentId, @PathVariable Long courseId) {
        return ResponseEntity.ok(studentService.getStudentCourseGrades(studentId, courseId));
    }

    @GetMapping("/grades/{studentId}/{assessmentId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Submission> getAssessmentGrade(@PathVariable Long studentId, @PathVariable Long assessmentId) {
        return ResponseEntity.ok(studentService.getAssessmentGrade(studentId, assessmentId));
    }




}