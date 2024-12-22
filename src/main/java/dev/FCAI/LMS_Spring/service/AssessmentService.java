package dev.FCAI.LMS_Spring.service;

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
    private SubmissionRepository submissionRepository;
    @Autowired
    private NotificationsService notificationService;

    @Transactional
    public Assessment getAssessmentForStudent(Long assessmentId, Long studentId) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        Course course = assessment.getCourse();
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        if(course.getEnrolledStudents().contains(student)) {
            Assessment studentAssessment = assessment;
            for (Question question : studentAssessment.getQuestions()) {
                if (question instanceof MCQ) {
                    MCQ mcq = (MCQ) question;
                    List<String> options = new ArrayList<>(mcq.getOptions());
                    Collections.shuffle(options);
                    mcq.setOptions(options);
                }
            }

            return studentAssessment;
        }
        else throw new RuntimeException("Student didn't enroll in this course");
    }

    @Transactional
    public void submitAssessment(Long assessmentId, Long studentId, Map<Long, String> answers) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = assessment.getCourse();
        if (course.getEnrolledStudents().contains(student)) {

            Submission submission = new Submission();
            submission.setAssessment(assessment);
            submission.setStudent(student);
            submission.setGraded(false);

            double totalScore = 0.0;

            for (Question question : assessment.getQuestions()) {
                SubmittedAnswer submittedAnswer = new SubmittedAnswer();
                submittedAnswer.setQuestion(question);
                String answerText = answers.get(question.getId());
                submittedAnswer.setAnswerText(answerText);
                double score = question.gradeQuestion(answerText);
                submittedAnswer.setAwardedScore(score);
                totalScore += score;
                submittedAnswer.setSubmission(submission);
                submission.getSubmittedAnswers().add(submittedAnswer);
            }
            submission.setTotalScore(totalScore);
            notificationService.notifyStudent(student,
                    "Assessment submitted: " + assessment.getTitle() + " - " + submission.getTotalScore());

            submissionRepository.save(submission);
        }
    }

    @Transactional
    public void submitAssignment(Long assessmentId, Long studentId, MultipartFile file) throws IOException {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        if (!(assessment instanceof Assignment)) {
            throw new RuntimeException("Assessment is not an Assignment");
        }

        Assignment assignment = (Assignment) assessment;

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = assignment.getCourse();

        if (!course.getEnrolledStudents().contains(student)) {
            throw new RuntimeException("Student is not enrolled in the course");
        }

        // Create a new Submission
        Submission submission = new Submission();
        submission.setAssessment(assignment);
        submission.setStudent(student);
        submission.setGraded(false);

        // Create a new SubmissionFile
        SubmissionFile submissionFile = new SubmissionFile();
        submissionFile.setFilename("student_" + studentId + "_" + file.getOriginalFilename());
        submissionFile.setData(file.getBytes());
        submissionFile.setSubmission(submission);

        submission.getSubmissionFiles().add(submissionFile);

        // Save the submission
        submissionRepository.save(submission);

        // Notify student
        notificationService.notifyStudent(student, "Assignment submitted successfully.");
    }

}