package dev.FCAI.LMS_Spring;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import dev.FCAI.LMS_Spring.service.StudentService;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private LessonMaterialRepository lessonMaterialRepository;
    @Mock
    private AssessmentRepository assessmentRepository;
    @InjectMocks
    private StudentService studentService;

    @Test
    void getEnrolledCourses_Success() {
        Student student = new Student();
        List<Course> courses = Arrays.asList(new Course(), new Course());
        student.setEnrolledCourses(courses);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        List<Course> result = studentService.getEnrolledCourses(1L);

        assertEquals(courses, result);
        assertEquals(2, result.size());
        verify(studentRepository).findById(1L);
    }

    @Test
    void getEnrolledCourses_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                studentService.getEnrolledCourses(1L)
        );
        verify(studentRepository).findById(1L);
    }

    @Test
    void enrollInCourse_Success() {
        Student student = new Student();
        student.setEnrolledCourses(new ArrayList<>());
        Course course = new Course();
        course.setEnrolledStudents(new ArrayList<>());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        studentService.enrollInCourse(1L, 1L);

        assertTrue(student.getEnrolledCourses().contains(course));
        assertTrue(course.getEnrolledStudents().contains(student));
        verify(studentRepository).save(student);
        verify(courseRepository).save(course);
    }

    @Test
    void enrollInCourse_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                studentService.enrollInCourse(1L, 1L)
        );
        verify(studentRepository).findById(1L);
        verify(courseRepository, never()).findById(any());
    }

    @Test
    void getEnrolledCourse_Success() {
        Student student = new Student();
        Course course = new Course();
        course.setEnrolledStudents(List.of(student));

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = studentService.getEnrolledCourse(1L, 1L);

        assertEquals(course, result);
        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
    }

    @Test
    void getEnrolledCourse_NotEnrolled() {
        Student student = new Student();
        Course course = new Course();
        course.setEnrolledStudents(new ArrayList<>());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        assertThrows(RuntimeException.class, () ->
                studentService.getEnrolledCourse(1L, 1L)
        );
    }

    @Test
    void getLesson_Success() {
        Student student = new Student();
        Course course = new Course();
        course.setEnrolledStudents(List.of(student));
        Lesson lesson = new Lesson();
        lesson.setCourse(course);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Lesson result = studentService.getLesson(1L, 1L);

        assertEquals(lesson, result);
        verify(lessonRepository).findById(1L);
        verify(studentRepository).findById(1L);
    }

    @Test
    void getLesson_StudentNotEnrolled() {
        Student student = new Student();
        Course course = new Course();
        course.setEnrolledStudents(new ArrayList<>());
        Lesson lesson = new Lesson();
        lesson.setCourse(course);

        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        assertThrows(RuntimeException.class, () ->
                studentService.getLesson(1L, 1L)
        );
    }

    @Test
    void getLessonMaterial_Success() {
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        LessonMaterial material = new LessonMaterial();
        material.setLesson(lesson);

        when(lessonMaterialRepository.findById(1L)).thenReturn(Optional.of(material));

        LessonMaterial result = studentService.getLessonMaterial(1L, 1L);

        assertEquals(material, result);
        verify(lessonMaterialRepository).findById(1L);
    }

    @Test
    void getLessonMaterial_MaterialNotFound() {
        when(lessonMaterialRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                studentService.getLessonMaterial(1L, 1L)
        );
    }

    @Test
    void getNotifications_Success() {
        Student student = new Student();
        List<Notification> notifications = Arrays.asList(
                new Notification(), new Notification()
        );
        student.setNotifications(notifications);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        List<Notification> result = studentService.getNotifications(1L);

        assertEquals(notifications, result);
        verify(studentRepository).findById(1L);
    }

    @Test
    void getNotifications_StudentNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                studentService.getNotifications(1L)
        );
    }

    @Test
    void startQuiz_Success() {
        // Create student and course
        Student student = new Student();
        student.setId(1L);
        Course course = new Course();
        course.setEnrolledStudents(List.of(student));

        // Create quiz with questions
        Quiz quiz = new Quiz();
        quiz.setCourse(course);
        quiz.setNumberOfQuestionsToAssign(2);
        List<Question> questions = Arrays.asList(
                createMCQQuestion(), createTrueFalseQuestion()
        );
        quiz.setQuestions(questions);

        // Create quiz submission
        QuizSubmission quizSubmission = new QuizSubmission();
        quizSubmission.setStudent(student);
        quizSubmission.setAssessment(quiz);
        List<SubmittedAnswer> submittedAnswers = new ArrayList<>();

        // Create submitted answers for each question
        for (Question question : questions) {
            SubmittedAnswer answer = new SubmittedAnswer();
            answer.setQuestion(question);
            answer.setSubmission(quizSubmission);
            submittedAnswers.add(answer);
        }
        quizSubmission.setSubmittedAnswers(submittedAnswers);

        // Mock repository calls
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(quiz));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(submissionRepository.save(any(QuizSubmission.class))).thenReturn(quizSubmission);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Execute method
        List<Question> result = studentService.startQuiz(1L, 1L);

        // Verify results
        assertEquals(2, result.size());
        verify(assessmentRepository).findById(1L);
        verify(studentRepository, times(2)).findById(1L); // Changed this line
        verify(submissionRepository).save(any(QuizSubmission.class));
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void submitQuiz_Success() {
        Student student = new Student();
        Quiz quiz = new Quiz();
        QuizSubmission submission = new QuizSubmission();
        submission.setStudent(student);
        submission.setAssessment(quiz);
        List<SubmittedAnswer> answers = new ArrayList<>();
        submission.setSubmittedAnswers(answers);

        Map<Long, String> submittedAnswers = new HashMap<>();
        submittedAnswers.put(1L, "answer1");

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));
        when(submissionRepository.save(any(QuizSubmission.class))).thenReturn(submission);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        Boolean result = studentService.submitQuiz(1L, submittedAnswers);

        assertTrue(result);
        assertTrue(submission.isGraded());
        verify(submissionRepository).save(submission);
    }

    @Test
    void submitAssignment_Success() throws IOException {
        Student student = new Student();
        Course course = new Course();
        course.setEnrolledStudents(List.of(student));
        Assignment assignment = new Assignment();
        assignment.setCourse(course);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.pdf");
        when(file.getBytes()).thenReturn("test content".getBytes());

        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assignment));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(submissionRepository.save(any(AssignmentSubmission.class))).thenReturn(new AssignmentSubmission());
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        AssignmentSubmission result = studentService.submitAssignment(1L, 1L, List.of(file));

        assertNotNull(result);
        verify(submissionRepository).save(any(AssignmentSubmission.class));
    }

    @Test
    void getStudentCourseGrades_Success() {
        Student student = new Student();
        Course course = new Course();
        List<Submission> submissions = Arrays.asList(
                new QuizSubmission(), new AssignmentSubmission()
        );

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(submissionRepository.findByStudentAndAssessment_Course(student, course))
                .thenReturn(submissions);

        List<Submission> result = studentService.getStudentCourseGrades(1L, 1L);

        assertEquals(submissions, result);
        assertEquals(2, result.size());
    }

    @Test
    void getAssessmentGrade_Success() {
        Student student = new Student();
        Assessment assessment = new Quiz();
        Submission submission = new QuizSubmission();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));
        when(submissionRepository.findByStudentAndAssessment(student, assessment))
                .thenReturn(List.of(submission));

        Submission result = studentService.getAssessmentGrade(1L, 1L);

        assertEquals(submission, result);
    }

    private MCQ createMCQQuestion() {
        MCQ question = new MCQ();
        question.setQuestionText("Test MCQ");
        question.setGrade(10.0);
        question.setOptions(Arrays.asList("A", "B", "C", "D"));
        question.setCorrectAnswer("A");
        return question;
    }

    private TrueFalse createTrueFalseQuestion() {
        TrueFalse question = new TrueFalse();
        question.setQuestionText("Test True/False");
        question.setGrade(5.0);
        question.setCorrectAnswer("true");
        return question;
    }
}