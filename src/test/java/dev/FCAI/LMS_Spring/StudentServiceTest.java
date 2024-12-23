package dev.FCAI.LMS_Spring;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import dev.FCAI.LMS_Spring.service.StudentService;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StudentServiceTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private SubmissionRepository submissionRepository;

    @MockBean
    private LessonRepository lessonRepository;

    @MockBean
    private LessonMaterialRepository lessonMaterialRepository;

    private Student student;
    private Course course;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setUsername("john_doe");
        student.setPassword("password123");
        student.setEmail("john.doe@example.com");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setEnrolledCourses(new ArrayList<>());
        student.setNotifications(new ArrayList<>());
        student.setSubmissions(new ArrayList<>());
        student.setAttendedLessons(new ArrayList<>());

        course = new Course();
        course.setId(1L);
        course.setTitle("Sample Course");
        course.setDescription("Sample Course Description");
        Instructor instructor = new Instructor();
        instructor.setId(2L);
        instructor.setFirstName("Jane");
        instructor.setLastName("Smith");
        instructor.setUsername("jane_smith");
        instructor.setPassword("password123");
        instructor.setEmail("instructor@gmail.com");
        instructor.setCreatedCourses(new ArrayList<>());
        course.setInstructor(instructor);
        course.setEnrolledStudents(new ArrayList<>());
        course.setLessons(new ArrayList<>());
        course.setAssessments(new ArrayList<>());
    }



    @Test
    void testGetEnrolledCourses_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(studentRepository.save(student)).thenReturn(student);
        when(courseRepository.save(course)).thenReturn(course);
        studentService.enrollInCourse(1L, 1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        List<Course> courses = studentService.getEnrolledCourses(1L);

        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals(course, courses.get(0));
    }

    @Test
    void testEnrollInCourse_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        studentService.enrollInCourse(1L, 1L);

        assertTrue(student.getEnrolledCourses().contains(course));
        assertTrue(course.getEnrolledStudents().contains(student));
        verify(studentRepository, times(2)).save(student);
        verify(courseRepository).save(course);
    }

    @Test
    void testGetEnrolledCourse_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        when(studentRepository.save(student)).thenReturn(student);
        when(courseRepository.save(course)).thenReturn(course);

        studentService.enrollInCourse(1L, 1L);

        Course resultCourse = studentService.getEnrolledCourse(1L, 1L);

        assertNotNull(resultCourse);
        assertEquals(course, resultCourse);
    }

    @Test
    void testGetLesson_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        when(studentRepository.save(student)).thenReturn(student);
        when(courseRepository.save(course)).thenReturn(course);

        studentService.enrollInCourse(1L, 1L);
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setTitle("Sample Lesson");
        lesson.setCourse(course);
        lesson.setMaterials(new ArrayList<>());
        lesson.setAttendedStudents(new ArrayList<>());
        course.getLessons().add(lesson);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        Lesson resultLesson = studentService.getLesson(1L, 1L);

        assertNotNull(resultLesson);
        assertEquals(lesson, resultLesson);
    }

    @Test
    void testGetLessonMaterial_Success() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setCourse(course);

        LessonMaterial material = new LessonMaterial();
        material.setId(1L);
        material.setLesson(lesson);
        material.setFilename("material.pdf");
        material.setData(new byte[]{});

        when(lessonMaterialRepository.findById(1L)).thenReturn(Optional.of(material));

        LessonMaterial resultMaterial = studentService.getLessonMaterial(1L, 1L);

        assertNotNull(resultMaterial);
        assertEquals(material, resultMaterial);
    }

    @Test
    void testGetNotifications_Success() {
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Test Notification");
        notification.setCreatedAt(java.time.LocalDateTime.now());
        notification.setRead(false);

        student.getNotifications().add(notification);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        List<Notification> notifications = studentService.getNotifications(1L);

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
        assertEquals(notification, notifications.get(0));
    }

    @Test
    void testViewSubmission_Success() {
        Submission submission = new Submission();
        submission.setId(1L);
        submission.setStudent(student);
        submission.setGraded(true);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));

        Submission result = studentService.viewSubmission(1L, 1L);

        assertNotNull(result);
        assertEquals(submission, result);
    }

    @Test
    void testGetSubmissions_Success() {
        Submission submission = new Submission();
        submission.setId(1L);
        student.getSubmissions().add(submission);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        List<Submission> submissions = studentService.getSubmissions(1L);

        assertNotNull(submissions);
        assertEquals(1, submissions.size());
        assertEquals(submission, submissions.get(0));
    }

    // Negative Test Cases

    @Test
    void testGetEnrolledCourses_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.getEnrolledCourses(1L);
        });

        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    void testEnrollInCourse_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.enrollInCourse(1L, 1L);
        });

        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    void testEnrollInCourse_CourseNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.enrollInCourse(1L, 1L);
        });

        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    void testGetEnrolledCourse_StudentNotEnrolled() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.getEnrolledCourse(1L, 1L);
        });

        assertEquals("Student didn't enroll in this course", exception.getMessage());
    }

    @Test
    void testGetLesson_StudentNotEnrolled() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Lesson lesson = new Lesson();
        lesson.setId(1L);
        lesson.setCourse(course);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.getLesson(1L, 1L);
        });

        assertEquals("Student didn't enroll in this course", exception.getMessage());
    }

    @Test
    void testGetLessonMaterial_LessonMismatch() {
        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);

        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);

        LessonMaterial material = new LessonMaterial();
        material.setId(1L);
        material.setLesson(lesson2);

        when(lessonMaterialRepository.findById(1L)).thenReturn(Optional.of(material));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.getLessonMaterial(1L, 1L);
        });

        assertEquals("Lesson material does not belong to the specified lesson", exception.getMessage());
    }

    @Test
    void testViewSubmission_NotAuthorized() {
        Student anotherStudent = new Student();
        anotherStudent.setId(2L);

        Submission submission = new Submission();
        submission.setId(1L);
        submission.setStudent(anotherStudent);
        submission.setGraded(true);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.viewSubmission(1L, 1L);
        });

        assertEquals("Not authorized to view this submission", exception.getMessage());
    }

    @Test
    void testViewSubmission_NotGraded() {
        Submission submission = new Submission();
        submission.setId(1L);
        submission.setStudent(student);
        submission.setGraded(false);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.viewSubmission(1L, 1L);
        });

        assertEquals("Submission is not yet graded", exception.getMessage());
    }

    @Test
    void testGetNotifications_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.getNotifications(1L);
        });

        assertEquals("Student not found", exception.getMessage());
    }

    @Test
    void testGetSubmissions_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.getSubmissions(1L);
        });

        assertEquals("Student not found", exception.getMessage());
    }
}