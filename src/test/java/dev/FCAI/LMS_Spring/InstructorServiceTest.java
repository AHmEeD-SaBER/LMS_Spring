package dev.FCAI.LMS_Spring;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import dev.FCAI.LMS_Spring.service.InstructorService;
import dev.FCAI.LMS_Spring.service.NotificationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServiceTest {
    @Mock
    private InstructorRepository instructorRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private InstructorService instructorService;

    @Test
    void createCourse_Success() {
        Instructor instructor = new Instructor();
        instructor.setId(1L);
        instructor.setCreatedCourses(new ArrayList<>());

        Course course = new Course();
        course.setTitle("Java Programming");

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = instructorService.createCourse(course, 1L);

        assertEquals(course, result);
        assertTrue(instructor.getCreatedCourses().contains(course));
        assertEquals(instructor, course.getInstructor());
        verify(instructorRepository).findById(1L);
        verify(courseRepository).save(course);
    }

    @Test
    void createCourse_InstructorNotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                instructorService.createCourse(new Course(), 1L)
        );
        verify(instructorRepository).findById(1L);
        verify(courseRepository, never()).save(any());
    }

    @Test
    void createAssessment_Success() {
        Instructor instructor = new Instructor();
        Course course = new Course();
        instructor.setCreatedCourses(List.of(course));

        Quiz assessment = new Quiz();
        assessment.setTitle("Java Quiz");
        assessment.setQuestions(new ArrayList<>());

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);

        Assessment result = instructorService.createAssessment(assessment, 1L, 1L);

        assertEquals(assessment, result);
        assertEquals(course, assessment.getCourse());
        assertTrue(course.getAssessments().contains(assessment));
        verify(assessmentRepository).save(assessment);
        verify(courseRepository).save(course);
    }

    @Test
    void createAssessment_InstructorNotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                instructorService.createAssessment(new Quiz(), 1L, 1L)
        );
        verify(instructorRepository).findById(1L);
        verify(assessmentRepository, never()).save(any());
    }

    @Test
    void createAssessment_CourseNotFound() {
        Instructor instructor = new Instructor();
        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                instructorService.createAssessment(new Quiz(), 1L, 1L)
        );
        verify(courseRepository).findById(1L);
        verify(assessmentRepository, never()).save(any());
    }

    @Test
    void createLesson_Success() {
        Instructor instructor = new Instructor();
        Course course = new Course();
        instructor.setCreatedCourses(List.of(course));

        Lesson lesson = new Lesson();
        lesson.setTitle("Introduction to Java");

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson result = instructorService.createLesson(lesson, 1L, 1L);

        assertEquals(lesson, result);
        assertEquals(course, lesson.getCourse());
        assertTrue(course.getLessons().contains(lesson));
        verify(lessonRepository).save(lesson);
    }

    @Test
    void createLesson_InstructorNotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                instructorService.createLesson(new Lesson(), 1L, 1L)
        );
        verify(instructorRepository).findById(1L);
        verify(lessonRepository, never()).save(any());
    }

    @Test
    void viewAllCourses_Success() {
        Instructor instructor = new Instructor();
        List<Course> courses = Arrays.asList(new Course(), new Course());
        instructor.setCreatedCourses(courses);

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        List<Course> result = instructorService.viewAllCourses(1L);

        assertEquals(courses, result);
        assertEquals(2, result.size());
        verify(instructorRepository).findById(1L);
    }

    @Test
    void viewAllCourses_InstructorNotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                instructorService.viewAllCourses(1L)
        );
        verify(instructorRepository).findById(1L);
    }

    @Test
    void updateCourse_Success() {
        Instructor instructor = new Instructor();
        Course course = new Course();
        instructor.setCreatedCourses(new ArrayList<>());

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course result = instructorService.updateCourse(course, 1L);

        assertEquals(course, result);
        assertEquals(instructor, course.getInstructor());
        verify(courseRepository).save(course);
    }

    @Test
    void updateCourse_InstructorNotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                instructorService.updateCourse(new Course(), 1L)
        );
        verify(instructorRepository).findById(1L);
        verify(courseRepository, never()).save(any());
    }

    @Test
    void deleteCourse_Success() {
        Instructor instructor = new Instructor();
        Course course = new Course();
        instructor.setCreatedCourses(List.of(course));

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        instructorService.deleteCourse(1L, 1L);

        verify(courseRepository).delete(course);
    }

    @Test
    void deleteCourse_InstructorNotFound() {
        when(instructorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                instructorService.deleteCourse(1L, 1L)
        );
        verify(instructorRepository).findById(1L);
        verify(courseRepository, never()).delete(any());
    }

    @Test
    void markStudentAttendance_Success() {
        Student student = new Student();
        Course course = new Course();
        course.setEnrolledStudents(List.of(student));
        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setAttendedStudents(new ArrayList<>());

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Boolean result = instructorService.markStudentAttendance(1L, 1L);

        assertTrue(result);
        assertTrue(lesson.getAttendedStudents().contains(student));
    }

    @Test
    void markStudentAttendance_LessonNotFound() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                instructorService.markStudentAttendance(1L, 1L)
        );
    }

    @Test
    void uploadLessonMaterial_Success() throws IOException {
        Instructor instructor = new Instructor();
        Course course = new Course();
        instructor.setCreatedCourses(List.of(course));
        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setMaterials(new ArrayList<>());

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.pdf");
        when(file.getBytes()).thenReturn("test content".getBytes());

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson result = instructorService.uploadLessonMaterial(1L, 1L, file);

        assertEquals(lesson, result);
        assertEquals(1, result.getMaterials().size());
        verify(lessonRepository).save(lesson);
    }

    @Test
    void gradeAssignment_Success() {
        Student student = new Student();
        Course course = new Course();
        Assignment assignment = new Assignment();
        assignment.setCourse(course);
        assignment.setGrade(100.0);

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssessment(assignment);
        submission.setStudent(student);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));
        when(submissionRepository.save(any(AssignmentSubmission.class))).thenReturn(submission);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        AssignmentSubmission result = instructorService.gradeAssignment(1L, 85.0, "Good work");

        assertEquals(submission, result);
        assertEquals(85.0, result.getTotalScore());
        assertEquals("Good work", result.getFeedback());
        assertTrue(result.isGraded());
    }

    @Test
    void gradeAssignment_SubmissionNotFound() {
        when(submissionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                instructorService.gradeAssignment(1L, 85.0, "Good work")
        );
    }
}