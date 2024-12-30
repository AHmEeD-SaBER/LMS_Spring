package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private LessonMaterialRepository lessonMaterialRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;

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
        return lessonMaterial;
    }

    public List<Notification> getNotifications(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return student.getNotifications();
    }


    public List<Question> startQuiz(Long quizId, Long studentId) {
        Quiz quiz = (Quiz) assessmentRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        if (!(quiz instanceof Quiz)) {
            throw new RuntimeException("Assessment is not a quiz");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = quiz.getCourse();
        if (!course.getEnrolledStudents().contains(student)) {
            throw new RuntimeException("Student is not enrolled in the course for this quiz");
        }
        List<Question> questionBank = new ArrayList<>(quiz.getQuestions());
        Collections.shuffle(questionBank);
        List<Question> selectedQuestions = questionBank.subList(0,
                Math.min(quiz.getNumberOfQuestionsToAssign(), questionBank.size()));

        QuizSubmission submission = new QuizSubmission();
        submission.setStudent(studentRepository.findById(studentId).orElseThrow());
        submission.setAssessment(quiz);
        List<SubmittedAnswer> submittedAnswers = selectedQuestions.stream()
                .map(question -> {
                    SubmittedAnswer answer = new SubmittedAnswer();
                    answer.setQuestion(question);
                    answer.setSubmission(submission);

                    if (question instanceof MCQ) {
                        MCQ mcq = (MCQ) question;
                        List<String> randomizedOptions = new ArrayList<>(mcq.getOptions());
                        Collections.shuffle(randomizedOptions);
                        mcq.setOptions(randomizedOptions);
                    }

                    return answer;
                })
                .collect(Collectors.toList());

        submission.setSubmittedAnswers(submittedAnswers);
        student.getSubmissions().add(submission);
        studentRepository.save(student);

        List<SubmittedAnswer> submissions = submissionRepository.save(submission).getSubmittedAnswers();
        return submissions.stream().map(SubmittedAnswer::getQuestion).collect(Collectors.toList());
    }

    public Boolean submitQuiz(Long submissionId, Map<Long, String> answers) {
        QuizSubmission submission = (QuizSubmission) submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.getSubmittedAnswers().forEach(answer -> {
            String studentAnswer = answers.get(answer.getQuestion().getId());
            if (studentAnswer != null) {
                answer.setAnswerText(studentAnswer);
                answer.setAwardedScore(answer.getQuestion().gradeQuestion(studentAnswer));
            }
        });

        double totalScore = submission.getSubmittedAnswers().stream()
                .mapToDouble(SubmittedAnswer::getAwardedScore)
                .sum();

        submission.setTotalScore(totalScore);
        submission.setGraded(true);
        submissionRepository.save(submission);
        Student student = submission.getStudent();
        student.getSubmissions().add(submission);
        studentRepository.save(student);

        return true;
    }

    public AssignmentSubmission submitAssignment(Long assignmentId, Long studentId,
                                                 List<MultipartFile> files) {
        Assignment assignment = (Assignment) assessmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!(assignment instanceof Assignment)) {
            throw new RuntimeException("Assessment is not a quiz");
        }
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = assignment.getCourse();
        if (!course.getEnrolledStudents().contains(student)) {
            throw new RuntimeException("Student is not enrolled in the course for this quiz");
        }

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setStudent(studentRepository.findById(studentId).orElseThrow());
        submission.setAssessment(assignment);

        List<SubmissionFile> submissionFiles = files.stream()
                .map(file -> {
                    try {
                        SubmissionFile submissionFile = new SubmissionFile();
                        submissionFile.setFilename(file.getOriginalFilename());
                        submissionFile.setData(file.getBytes());
                        submissionFile.setSubmission(submission);
                        return submissionFile;
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to process file", e);
                    }
                })
                .collect(Collectors.toList());

        submission.setSubmissionFiles(submissionFiles);
        student.getSubmissions().add(submission);
        studentRepository.save(student);
        return submissionRepository.save(submission);
    }

    public List<Submission> getStudentCourseGrades(Long studentId, Long courseId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return submissionRepository.findByStudentAndAssessment_Course(student, course);
    }

    public Submission getAssessmentGrade(Long studentId, Long assessmentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        return submissionRepository.findByStudentAndAssessment(student, assessment)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No submission found"));
    }
}