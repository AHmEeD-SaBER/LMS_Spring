package dev.FCAI.LMS_Spring;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import dev.FCAI.LMS_Spring.service.AssessmentService;
import dev.FCAI.LMS_Spring.service.NotificationsService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AssessmentServiceTest {

    @Autowired
    private AssessmentService assessmentService;

    @MockBean
    private AssessmentRepository assessmentRepository;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private SubmissionRepository submissionRepository;

    private Student student;
    private Course course;
    private Quiz quizAssessment;
    private Assignment assignmentAssessment;

    @BeforeEach
    void setUp() {
        // Initialize sample data

        // Create Student
        student = new Student();
        student.setId(1L);
        student.setUsername("john_student");
        student.setPassword("password");
        student.setEmail("john.student@example.com");
        student.setFirstName("John");
        student.setLastName("Student");
        student.setEnrolledCourses(new ArrayList<>());
        student.setSubmissions(new ArrayList<>());
        student.setNotifications(new ArrayList<>());
        student.setAttendedLessons(new ArrayList<>());

        // Create Instructor
        Instructor instructor = new Instructor();
        instructor.setId(2L);
        instructor.setUsername("jane_instructor");
        instructor.setPassword("password");
        instructor.setEmail("jane.instructor@example.com");
        instructor.setFirstName("Jane");
        instructor.setLastName("Instructor");
        instructor.setCreatedCourses(new ArrayList<>());
        instructor.setNotifications(new ArrayList<>());

        // Create Course and assign to Instructor
        course = new Course();
        course.setId(1L);
        course.setTitle("Sample Course");
        course.setDescription("Description of Sample Course");
        course.setInstructor(instructor);
        course.setEnrolledStudents(new ArrayList<>());
        course.setAssessments(new ArrayList<>());
        course.setLessons(new ArrayList<>());

        // Enroll student in course
        student.getEnrolledCourses().add(course);
        course.getEnrolledStudents().add(student);

        // Create Quiz Assessment
        quizAssessment = new Quiz();
        quizAssessment.setId(1L);
        quizAssessment.setTitle("Sample Quiz");
        quizAssessment.setGrade(100.0);
        quizAssessment.setCourse(course);
        quizAssessment.setQuestions(new ArrayList<>());
        quizAssessment.setSubmissions(new ArrayList<>());

        // Add assessment to course
        course.getAssessments().add(quizAssessment);

        // Create Assignment Assessment
        assignmentAssessment = new Assignment();
        assignmentAssessment.setId(2L);
        assignmentAssessment.setTitle("Sample Assignment");
        assignmentAssessment.setGrade(100.0);
        assignmentAssessment.setCourse(course);
        assignmentAssessment.setQuestions(new ArrayList<>());
        assignmentAssessment.setSubmissions(new ArrayList<>());

        // Add assessment to course
        course.getAssessments().add(assignmentAssessment);

    }

    // -----------------------------
    // Test getAssessmentForStudent(Long assessmentId, Long studentId)
    // -----------------------------

    @Test
    void testGetAssessmentForStudent_Success() {
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(quizAssessment));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Add a question to the quiz
        MCQ question = new MCQ();
        question.setId(1L);
        question.setQuestionText("What is 2 + 2?");
        question.setCorrectAnswer("4");
        question.setGrade(10.0);
        question.setOptions(Arrays.asList("1", "2", "3", "4"));
        question.setAssessment(quizAssessment);
        quizAssessment.getQuestions().add(question);

        Assessment result = assessmentService.getAssessmentForStudent(1L, 1L);

        assertNotNull(result);
        assertEquals(quizAssessment.getTitle(), result.getTitle());
        assertEquals(1, result.getQuestions().size());

        // Verify that options are shuffled (randomness makes this hard to test)
        MCQ resultQuestion = (MCQ) result.getQuestions().get(0);
        assertNotNull(resultQuestion.getOptions());
        assertEquals(4, resultQuestion.getOptions().size());
    }

    @Test
    void testGetAssessmentForStudent_NotEnrolled() {
        // Student is not enrolled in the course
        student.getEnrolledCourses().remove(course);
        course.getEnrolledStudents().remove(student);

        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(quizAssessment));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            assessmentService.getAssessmentForStudent(1L, 1L);
        });

        assertEquals("Student didn't enroll in this course", exception.getMessage());
    }

    @Test
    void testGetAssessmentForStudent_AssessmentNotFound() {
        when(assessmentRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            assessmentService.getAssessmentForStudent(99L, 1L);
        });

        assertEquals("Assessment not found", exception.getMessage());
    }

    // -----------------------------
    // Test submitAssessment(Long assessmentId, Long studentId, Map<Long, String> answers)
    // -----------------------------

    @Test
    @Transactional
    void testSubmitAssessment_Success() {
        // Save the student before the test
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        studentRepository.save(student);

        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(quizAssessment));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Add questions to the quiz
        MCQ question1 = new MCQ();
        question1.setId(1L);
        question1.setQuestionText("What is 2 + 2?");
        question1.setCorrectAnswer("4");
        question1.setGrade(10.0);
        question1.setOptions(Arrays.asList("2", "3", "4", "5"));
        question1.setAssessment(quizAssessment);

        TrueFalse question2 = new TrueFalse();
        question2.setId(2L);
        question2.setQuestionText("The sky is blue.");
        question2.setCorrectAnswer("True");
        question2.setGrade(5.0);
        question2.setAssessment(quizAssessment);

        quizAssessment.getQuestions().addAll(Arrays.asList(question1, question2));

        // Student's answers
        Map<Long, String> answers = new HashMap<>();
        answers.put(1L, "4");
        answers.put(2L, "True");

        assessmentService.submitAssessment(1L, 1L, answers);

        // Capture the submission saved
        ArgumentCaptor<Submission> submissionCaptor = ArgumentCaptor.forClass(Submission.class);
        verify(submissionRepository).save(submissionCaptor.capture());

        Submission savedSubmission = submissionCaptor.getValue();
        assertNotNull(savedSubmission);
        assertEquals(student, savedSubmission.getStudent());
        assertEquals(quizAssessment, savedSubmission.getAssessment());
        assertEquals(15.0, savedSubmission.getTotalScore());
        assertEquals(2, savedSubmission.getSubmittedAnswers().size());
        assertFalse(savedSubmission.isGraded()); // Assuming submissions are not auto-graded

    }



    @Test
    void testSubmitAssessment_AssessmentNotFound() {
        when(assessmentRepository.findById(99L)).thenReturn(Optional.empty());

        Map<Long, String> answers = new HashMap<>();

        Exception exception = assertThrows(RuntimeException.class, () -> {
            assessmentService.submitAssessment(99L, 1L, answers);
        });

        assertEquals("Assessment not found", exception.getMessage());
    }

    @Test
    @Transactional
    void testSubmitAssignment_Success() throws IOException {
        // Save the student before the test
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        studentRepository.save(student);

        when(assessmentRepository.findById(2L)).thenReturn(Optional.of(assignmentAssessment));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Mock file upload
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("assignment.docx");
        when(mockFile.getBytes()).thenReturn(new byte[]{1, 2, 3});

        assessmentService.submitAssignment(2L, 1L, mockFile);

        // Capture the submission saved
        ArgumentCaptor<Submission> submissionCaptor = ArgumentCaptor.forClass(Submission.class);
        verify(submissionRepository).save(submissionCaptor.capture());

        Submission savedSubmission = submissionCaptor.getValue();
        assertNotNull(savedSubmission);
        assertEquals(student, savedSubmission.getStudent());
        assertEquals(assignmentAssessment, savedSubmission.getAssessment());
        assertEquals(0.0, savedSubmission.getTotalScore());
        assertFalse(savedSubmission.isGraded());
        assertEquals(1, savedSubmission.getSubmissionFiles().size());

        SubmissionFile savedFile = savedSubmission.getSubmissionFiles().get(0);
        assertEquals("student_1_assignment.docx", savedFile.getFilename());
        assertArrayEquals(new byte[]{1, 2, 3}, savedFile.getData());

    }

    @Test
    void testSubmitAssignment_AssessmentNotFound() {
        when(assessmentRepository.findById(99L)).thenReturn(Optional.empty());

        MultipartFile mockFile = mock(MultipartFile.class);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            assessmentService.submitAssignment(99L, 1L, mockFile);
        });

        assertEquals("Assessment not found", exception.getMessage());
    }

    @Test
    void testSubmitAssignment_NotAnAssignment() {
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(quizAssessment));

        MultipartFile mockFile = mock(MultipartFile.class);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            assessmentService.submitAssignment(1L, 1L, mockFile);
        });

        assertEquals("Assessment is not an Assignment", exception.getMessage());
    }

    @Test
    void testSubmitAssignment_StudentNotEnrolled() {
        // Student is not enrolled
        student.getEnrolledCourses().remove(course);
        course.getEnrolledStudents().remove(student);

        when(assessmentRepository.findById(2L)).thenReturn(Optional.of(assignmentAssessment));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        MultipartFile mockFile = mock(MultipartFile.class);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            assessmentService.submitAssignment(2L, 1L, mockFile);
        });

        assertEquals("Student is not enrolled in the course", exception.getMessage());
    }

    @Test
    void testSubmitAssignment_FileIOException() throws IOException {
        when(assessmentRepository.findById(2L)).thenReturn(Optional.of(assignmentAssessment));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("assignment.docx");
        when(mockFile.getBytes()).thenThrow(new IOException("File read error"));

        Exception exception = assertThrows(IOException.class, () -> {
            assessmentService.submitAssignment(2L, 1L, mockFile);
        });

        assertEquals("File read error", exception.getMessage());
    }

    // Additional test methods can be added here to cover other scenarios

}
