package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private NotificatiosService notificationPublisher;

    @Transactional
    public Course createCourse(Course course, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new RuntimeException("Instructor not found"));
        if (instructor.getCreatedCourses().contains(course)) return course;
        instructor.getCreatedCourses().add(course);
        instructorRepository.save(instructor);
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }


    @Transactional
    public void deleteCourse(Long courseId, Long instructorId) {
        if (courseRepository.findById(courseId).isEmpty()) return;
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new RuntimeException("Instructor not found"));
        if (instructor.getCreatedCourses().contains(courseRepository.findById(courseId).get())) {
            courseRepository.deleteById(courseId);
        } else throw new RuntimeException("Course not found");
    }

    @Transactional
    public Course updateCourse(Course course, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new RuntimeException("Instructor not found"));
        if (courseRepository.findById(course.getId()).isEmpty()) throw new RuntimeException("Course not found");
        if (instructor.getCreatedCourses().contains(course)) {
            return courseRepository.save(course);
        } else throw new RuntimeException("Course not found");
    }

    @Transactional
    public Assessment createAssessment(Assessment assessment, Long courseId, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new RuntimeException("Instructor not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));
        if (!instructor.getCreatedCourses().contains(course)) {
            throw new RuntimeException("Instructor does not own the course");
        }
        assessment.setCourse(course);
        course.getAssessments().add(assessment);


        List<Student> enrolledStudents = course.getEnrolledStudents();
        for (Student student : enrolledStudents) {
            notificationPublisher.notifyStudent(student,
                    "New Assessment Published: " + course.getTitle() + " - " + assessment.getTitle());
        }

        courseRepository.save(course);
        System.out.println("Creating Assessment: =" + assessment.getType());
        return assessmentRepository.save(assessment);
    }

    private void validateQuiz(Quiz quiz) {
        if (quiz.getTotalQuestions() == null || quiz.getTotalQuestions() <= 0) {
            throw new IllegalArgumentException("Total questions must be greater than 0");
        }
        if (quiz.getPassingScore() == null || quiz.getPassingScore() < 0) {
            throw new IllegalArgumentException("Passing score must be 0 or greater");
        }
    }

    private void validateAssignment(Assignment assignment) {
        if (assignment.getSubmissionPath() == null || assignment.getSubmissionPath().isEmpty()) {
            throw new IllegalArgumentException("Submission path cannot be empty");
        }
    }

    public List<Assessment> viewAllAssessmentsGrades(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        return courseOptional.map(Course::getAssessments).orElse(List.of());
    }

    public List<Course> viewAllCourses(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new RuntimeException("Instructor not found"));
        return instructor.getCreatedCourses();
    }

    public Lesson createLesson(Lesson lesson, Long courseId, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!instructor.getCreatedCourses().contains(course)) {
            throw new RuntimeException("Instructor does not own this course");
        }

        lesson.setCourse(course);
        List<Student> enrolledStudents = course.getEnrolledStudents();
        for (Student student : enrolledStudents) {
            notificationPublisher.notifyStudent(student,
                    "New Lesson Added: " + course.getTitle() + " - " + lesson.getTitle());
        }

        return lessonRepository.save(lesson);
    }

    @Transactional
    public Boolean markStudentAttendance(Long lessonId, Long studentId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson with ID " + lessonId + " not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student with ID " + studentId + " not found"));
        Course course = lesson.getCourse();
        if (!course.getEnrolledStudents().contains(student)) {
            throw new RuntimeException("Student " + studentId + " is not enrolled in the course for this lesson.");
        }

        if (!lesson.getAttendedStudents().contains(student)) {
            lesson.getAttendedStudents().add(student);
            return true;
        }
        return false;

    }
    }

