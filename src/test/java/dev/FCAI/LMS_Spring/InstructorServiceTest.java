package dev.FCAI.LMS_Spring;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import dev.FCAI.LMS_Spring.service.InstructorService;
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
public class InstructorServiceTest {

    @InjectMocks
    private InstructorService instructorService;

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

    private Instructor instructor;
    private Course course;
    private Assessment assessment;
    private Lesson lesson;
    private Student student;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize entities
        instructor = new Instructor();
        instructor.setId(1L);
        instructor.setCreatedCourses(new ArrayList<>());

        course = new Course();
        course.setId(1L);
        course.setTitle("Test Course");
        course.setInstructor(instructor);
        course.setEnrolledStudents(new ArrayList<>());
        instructor.getCreatedCourses().add(course);

        student = new Student();
        student.setId(1L);
        student.setUsername("student1");

        assessment = new Quiz();
        assessment.setId(1L);
        assessment.setTitle("Test Assessment");
        assessment.setCourse(course);
        assessment.setQuestions(new ArrayList<>());

        lesson = new Lesson();
        lesson.setId(1L);
        lesson.setTitle("Test Lesson");
        lesson.setCourse(course);
        lesson.setMaterials(new ArrayList<>());
        lesson.setAttendedStudents(new ArrayList<>());

        course.setAssessments(new ArrayList<>());
        course.setLessons(new ArrayList<>());

        course.getEnrolledStudents().add(student);
    }

    @Test
    public void testCreateCourse_Success() {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course newCourse = new Course();
        newCourse.setTitle("New Course");

        Course savedCourse = instructorService.createCourse(newCourse, instructor.getId());

        assertNotNull(savedCourse);
        assertEquals("New Course", savedCourse.getTitle());
        assertEquals(instructor, savedCourse.getInstructor());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    public void testCreateCourse_InstructorNotFound() {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Course newCourse = new Course();
        newCourse.setTitle("New Course");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            instructorService.createCourse(newCourse, 999L);
        });

        assertEquals("Instructor not found", exception.getMessage());
        verify(courseRepository, times(0)).save(any(Course.class));
    }

    @Test
    public void testCreateAssessment_Success() {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(assessment);

        Assessment newAssessment = new Quiz();
        newAssessment.setTitle("New Assessment");
        newAssessment.setQuestions(new ArrayList<>());

        Assessment savedAssessment = instructorService.createAssessment(newAssessment, course.getId(), instructor.getId());

        assertNotNull(savedAssessment);
        assertEquals("New Assessment", savedAssessment.getTitle());
        assertEquals(course, savedAssessment.getCourse());
        verify(assessmentRepository, times(1)).save(any(Assessment.class));
    }

    @Test
    public void testCreateAssessment_InstructorDoesNotOwnCourse() {
        Instructor otherInstructor = new Instructor();
        otherInstructor.setId(2L);
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(otherInstructor));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        Assessment newAssessment = new Quiz();
        newAssessment.setTitle("New Assessment");
        newAssessment.setQuestions(new ArrayList<>());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            instructorService.createAssessment(newAssessment, course.getId(), otherInstructor.getId());
        });

        assertEquals("Instructor does not own the course", exception.getMessage());
        verify(assessmentRepository, times(0)).save(any(Assessment.class));
    }

    @Test
    public void testCreateLesson_Success() {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        Lesson newLesson = new Lesson();
        newLesson.setTitle("New Lesson");

        Lesson savedLesson = instructorService.createLesson(newLesson, course.getId(), instructor.getId());

        assertNotNull(savedLesson);
        assertEquals("New Lesson", savedLesson.getTitle());
        assertEquals(course, savedLesson.getCourse());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    public void testCreateLesson_InstructorDoesNotOwnCourse() {
        Instructor otherInstructor = new Instructor();
        otherInstructor.setId(2L);
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(otherInstructor));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        Lesson newLesson = new Lesson();
        newLesson.setTitle("New Lesson");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            instructorService.createLesson(newLesson, course.getId(), otherInstructor.getId());
        });

        assertEquals("Instructor does not own this course", exception.getMessage());
        verify(lessonRepository, times(0)).save(any(Lesson.class));
    }

    @Test
    public void testViewAllCourses_Success() {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));

        List<Course> courses = instructorService.viewAllCourses(instructor.getId());

        assertNotNull(courses);
        assertEquals(1, courses.size());
        assertEquals(course, courses.get(0));
    }

    @Test
    public void testViewAllCourses_InstructorNotFound() {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            instructorService.viewAllCourses(999L);
        });

        assertEquals("Instructor not found", exception.getMessage());
    }

    @Test
    public void testUpdateCourse_Success() {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course updatedCourse = new Course();
        updatedCourse.setId(course.getId());
        updatedCourse.setTitle("Updated Title");

        Course savedCourse = instructorService.updateCourse(updatedCourse, instructor.getId());

        assertNotNull(savedCourse);
        assertEquals("Updated Title", savedCourse.getTitle());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    public void testUpdateCourse_InstructorDoesNotOwnCourse() {
        Instructor otherInstructor = new Instructor();
        otherInstructor.setId(2L);
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(otherInstructor));

        Course updatedCourse = new Course();
        updatedCourse.setId(course.getId());
        updatedCourse.setTitle("Updated Title");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            instructorService.updateCourse(updatedCourse, otherInstructor.getId());
        });

        assertEquals("Instructor does not own this course", exception.getMessage());
        verify(courseRepository, times(0)).save(any(Course.class));
    }

    @Test
    public void testDeleteCourse_Success() {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        instructorService.deleteCourse(course.getId(), instructor.getId());

        verify(courseRepository, times(1)).delete(any(Course.class));
    }

    @Test
    public void testDeleteCourse_InstructorDoesNotOwnCourse() {
        Instructor otherInstructor = new Instructor();
        otherInstructor.setId(2L);
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(otherInstructor));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            instructorService.deleteCourse(course.getId(), otherInstructor.getId());
        });

        assertEquals("Instructor does not own this course", exception.getMessage());
        verify(courseRepository, times(0)).delete(any(Course.class));
    }

    @Test
    public void testMarkStudentAttendance_Success() {
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(student));

        Boolean result = instructorService.markStudentAttendance(lesson.getId(), student.getId());

        assertTrue(result);
        assertTrue(lesson.getAttendedStudents().contains(student));
    }

    @Test
    public void testMarkStudentAttendance_StudentNotEnrolled() {
        Student otherStudent = new Student();
        otherStudent.setId(2L);
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(otherStudent));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            instructorService.markStudentAttendance(lesson.getId(), otherStudent.getId());
        });

        assertEquals("Student 2 is not enrolled in the course for this lesson.", exception.getMessage());
    }

    @Test
    public void testUploadLessonMaterial_Success() throws IOException {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any(Lesson.class))).thenReturn(lesson);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("testfile.pdf");
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        Lesson updatedLesson = instructorService.uploadLessonMaterial(instructor.getId(), lesson.getId(), file);

        assertNotNull(updatedLesson);
        assertEquals(1, updatedLesson.getMaterials().size());
        LessonMaterial material = updatedLesson.getMaterials().get(0);
        assertEquals("lesson_" + lesson.getId() + "_testfile.pdf", material.getFilename());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    public void testUploadLessonMaterial_InstructorDoesNotOwnCourse() throws IOException {
        Instructor otherInstructor = new Instructor();
        otherInstructor.setId(2L);
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(otherInstructor));
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));

        MultipartFile file = mock(MultipartFile.class);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            instructorService.uploadLessonMaterial(otherInstructor.getId(), lesson.getId(), file);
        });

        assertEquals("Instructor does not own this course", exception.getMessage());
        verify(lessonRepository, times(0)).save(any(Lesson.class));
    }
}