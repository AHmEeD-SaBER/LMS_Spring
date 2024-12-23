package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private NotificationsService notificationService;
    @Autowired
    private BaseSubmissionRepository submissionRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LessonMaterialRepository lessonMaterialRepository;

    public List<Course> getEnrolledCourses(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getEnrolledCourses();
    }

    @Transactional
    public void enrollInCourse(Long courseId, Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        student.getEnrolledCourses().add(course);
        course.getEnrolledStudents().add(student);
        Instructor instructor = course.getInstructor();

        notificationService.notifyStudent(student,
                "You have successfully enrolled in course: " + course.getTitle());

        notificationService.notifyInstructor(instructor,
                "Student " + student.getFirstName() + " " + student.getLastName() +
                        " has enrolled in your course: " + course.getTitle());

        studentRepository.save(student);
        courseRepository.save(course);
    }

    public Course getEnrolledCourse(Long courseId, Long studentId){
        Student  student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        if(course.getEnrolledStudents().contains(student)){
            return course;
        }
        else throw new RuntimeException("Student didn't enroll in this course");
    }

    public Lesson getLesson(Long lessonId, Long studentId){
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(() -> new RuntimeException("Lesson not found"));
        Student  student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = lesson.getCourse();
        if(course.getEnrolledStudents().contains(student)){
            return lesson;
        }
        else throw new RuntimeException("Student didn't enroll in this course");
    }

    public LessonMaterial getLessonMaterial(Long lessonId, Long materialId) {
        LessonMaterial lessonMaterial = lessonMaterialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Lesson material not found"));
        if (!lessonMaterial.getLesson().getId().equals(lessonId)) {
            throw new RuntimeException("Lesson material does not belong to the specified lesson");
        }
        // Additional checks to ensure the student is enrolled in the course
        return lessonMaterial;
    }

    public List<Notification> getNotifications(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getNotifications();
    }

    public BaseSubmission viewSubmission(Long submissionId, Long studentId) {
        BaseSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));
        if (!submission.getStudent().getId().equals(studentId)) {
            throw new RuntimeException("Not authorized to view this submission");
        }
        if (!submission.isGraded()) {
            throw new RuntimeException("Submission is not yet graded");
        }
        return submission;
    }

    public List<BaseSubmission> getSubmissions(Long studentId){
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getSubmissions();
    }
}