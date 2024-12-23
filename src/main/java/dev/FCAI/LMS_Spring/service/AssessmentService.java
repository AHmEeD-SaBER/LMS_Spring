package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.dto.response.StudentQuestionResponseDTO;
import dev.FCAI.LMS_Spring.dto.response.StudentQuizResponseDTO;
import dev.FCAI.LMS_Spring.dto.response.SubmitAssessmentResponseDTO;
import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class AssessmentService {
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private BaseSubmissionRepository submissionRepository;
    @Autowired
    private NotificationsService notificationService;
    @Autowired
    private StudentQuizRepository studentQuizRepository;
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    private BaseSubmissionRepository baseSubmissionRepository;


    @Transactional
    public StudentQuizResponseDTO getAssessmentForStudent(Long assessmentId, Long studentId) {
        // Fetch the quiz
        Quiz quiz = (Quiz) assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        // Fetch the student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Verify enrollment
        if (!quiz.getCourse().getEnrolledStudents().contains(student)) {
            throw new RuntimeException("Student is not enrolled in the course");
        }

        // Check if a StudentQuiz already exists for this student and quiz
        StudentQuiz studentQuiz = studentQuizRepository.findByStudentAndQuiz(student, quiz)
                .orElse(null);

        if (studentQuiz == null) {
            studentQuiz = new StudentQuiz();
            studentQuiz.setStudent(student);
            studentQuiz.setQuiz(quiz);
            List<Question> questionBank = quiz.getQuestionBank();
            int numQuestions = quiz.getNumQuestions();

            if (questionBank.size() < numQuestions) {
                throw new RuntimeException("Not enough questions in the question bank");
            }

            List<Question> randomizedQuestions = new ArrayList<>(questionBank);
            Collections.shuffle(randomizedQuestions);
            List<Question> selectedQuestions = randomizedQuestions.subList(0, numQuestions);
            studentQuiz.setQuestions(selectedQuestions);
            studentQuizRepository.save(studentQuiz);
        }

        // Prepare the response DTO
        StudentQuizResponseDTO quizResponseDTO = new StudentQuizResponseDTO();
        quizResponseDTO.setQuizId(quiz.getId());
        quizResponseDTO.setTitle(quiz.getTitle());

        // Map questions to DTOs
        List<StudentQuestionResponseDTO> questionDTOs = new ArrayList<>();
        for (Question question : studentQuiz.getQuestions()) {
            StudentQuestionResponseDTO questionDTO = new StudentQuestionResponseDTO();
            questionDTO.setId(question.getId());
            questionDTO.setQuestionText(question.getQuestionText());
            questionDTO.setQuestionType(question.getQuestionType());

            if (question instanceof MCQ) {
                MCQ mcq = (MCQ) question;
                List<String> options = new ArrayList<>(mcq.getOptions());
                Collections.shuffle(options);
                questionDTO.setOptions(options);
            }

            // For other question types, handle accordingly

            questionDTOs.add(questionDTO);
        }

        quizResponseDTO.setQuestions(questionDTOs);

        return quizResponseDTO;
    }

    @Transactional
    public void submitAssessment(Long assessmentId, Long studentId, Map<Long, String> answers) {
        Quiz quiz = (Quiz) assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentQuiz studentQuiz = studentQuizRepository.findByStudentAndQuiz(student, quiz)
                .orElseThrow(() -> new RuntimeException("No quiz found for this student"));

        if (studentQuiz.getSubmission() != null) {
            throw new RuntimeException("Student has already submitted this assessment");
        }

        QuizSubmission submission = new QuizSubmission();
        submission.setStudentQuiz(studentQuiz);
        submission.setGraded(false);

        double totalScore = 0.0;

        for (Question question : studentQuiz.getQuestions()) {
            SubmittedAnswer submittedAnswer = new SubmittedAnswer();
            submittedAnswer.setQuestion(question);
            String answerText = answers.get(question.getId());
            if (answerText == null) {
                throw new RuntimeException("Answer for question ID " + question.getId() + " is missing");
            }
            submittedAnswer.setAnswerText(answerText);

            totalScore += question.gradeQuestion(answerText);
            submittedAnswer.setAwardedScore(question.gradeQuestion(answerText));
            submittedAnswer.setSubmission(submission);
            submission.getSubmittedAnswers().add(submittedAnswer);
        }

        submission.setTotalScore(totalScore);
        studentQuiz.setSubmission(submission);
        submissionRepository.save(submission);
        studentQuizRepository.save(studentQuiz);
    }

    @Transactional
    public SubmitAssessmentResponseDTO submitAssignment(Long assignmentId, Long studentId, MultipartFile file) throws IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!assignment.getCourse().getEnrolledStudents().contains(student)) {
            throw new RuntimeException("Student is not enrolled in the course");
        }

        // Check if already submitted
        boolean alreadySubmitted = assignment.getSubmissions().stream()
                .anyMatch(sub -> sub.getStudent().getId().equals(studentId));

        if (alreadySubmitted) {
            throw new RuntimeException("Assignment has already been submitted");
        }

        // Save the file to storage and get the file path or URL
        String submissionFilePath = fileStorageService.storeFile(file, "assignments/" + assignmentId + "/students/" + studentId);

        // Create a new AssignmentSubmission
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setGraded(false);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setSubmissionFilePath(submissionFilePath);

        baseSubmissionRepository.save(submission);

        // Notify student
        notificationService.notifyStudent(student, "Assignment submitted successfully.");

        // Prepare response DTO
        SubmitAssessmentResponseDTO responseDTO = new SubmitAssessmentResponseDTO();
        responseDTO.setMessage("Assignment submitted successfully.");

        return responseDTO;
    }

}