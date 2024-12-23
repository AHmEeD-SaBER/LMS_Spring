package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.dto.*;
import dev.FCAI.LMS_Spring.dto.request.*;
import dev.FCAI.LMS_Spring.dto.response.*;
import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstructorService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Transactional
    public CourseResponseDTO createCourse(CourseRequestDTO courseRequestDTO) {
        // Validate input
        if (courseRequestDTO == null) {
            throw new IllegalArgumentException("Course data must not be null.");
        }

        if (courseRequestDTO.getInstructorId() == null) {
            throw new IllegalArgumentException("Instructor ID is required.");
        }

        Instructor instructor = instructorRepository.findById(courseRequestDTO.getInstructorId())
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found with ID: " + courseRequestDTO.getInstructorId()));

        Course course = new Course();
        course.setTitle(courseRequestDTO.getTitle());
        course.setDescription(courseRequestDTO.getDescription());
        course.setInstructor(instructor);

        courseRepository.save(course);

        return mapToCourseResponseDTO(course);
    }

    @Transactional
    public CourseResponseDTO updateCourse(Long courseId, CourseRequestDTO courseRequestDTO) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        if (courseRequestDTO.getTitle() != null) {
            course.setTitle(courseRequestDTO.getTitle());
        }
        if (courseRequestDTO.getDescription() != null) {
            course.setDescription(courseRequestDTO.getDescription());
        }

        courseRepository.save(course);

        return mapToCourseResponseDTO(course);
    }

    public CourseResponseDTO getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));

        return mapToCourseResponseDTO(course);
    }

    public List<CourseResponseDTO> getCoursesByInstructor(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new IllegalArgumentException("Instructor not found with ID: " + instructorId));

        List<Course> courses = instructor.getCreatedCourses();

        return courses.stream()
                .map(this::mapToCourseResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Transactional
    public LessonResponseDTO createLesson(LessonRequestDTO lessonRequestDTO) {
        if (lessonRequestDTO == null || lessonRequestDTO.getCourseId() == null) {
            throw new IllegalArgumentException("Lesson data and course ID must not be null.");
        }

        Course course = courseRepository.findById(lessonRequestDTO.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + lessonRequestDTO.getCourseId()));

        Lesson lesson = new Lesson();
        lesson.setTitle(lessonRequestDTO.getTitle());
        lesson.setCourse(course);

        lessonRepository.save(lesson);

        return mapToLessonResponseDTO(lesson);
    }

    @Transactional
    public AssessmentResponseDTO createAssessment(AssessmentRequestDTO assessmentDTO) {
        if (assessmentDTO == null || assessmentDTO.getCourseId() == null) {
            throw new IllegalArgumentException("Assessment data and course ID must not be null.");
        }

        Course course = courseRepository.findById(assessmentDTO.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + assessmentDTO.getCourseId()));

        Assessment assessment;

        switch (assessmentDTO.getType().toUpperCase()) {
            case "QUIZ":
                if (!(assessmentDTO instanceof QuizRequestDTO)) {
                    throw new IllegalArgumentException("Invalid data for quiz.");
                }
                QuizRequestDTO quizDTO = (QuizRequestDTO) assessmentDTO;
                Quiz quiz = new Quiz();
                quiz.setTitle(quizDTO.getTitle());
                quiz.setGrade(quizDTO.getGrade());
                quiz.setNumQuestions(quizDTO.getNumQuestions());
                quiz.setCourse(course);
                assessment = quiz;
                break;
            case "ASSIGNMENT":
                Assignment assignment = new Assignment();
                assignment.setTitle(assessmentDTO.getTitle());
                assignment.setGrade(assessmentDTO.getGrade());
                assignment.setCourse(course);
                // Set other assignment-specific fields
                assessment = assignment;
                break;
            default:
                throw new IllegalArgumentException("Invalid assessment type: " + assessmentDTO.getType());
        }

        assessmentRepository.save(assessment);

        return mapToAssessmentResponseDTO(assessment);
    }

    @Transactional
    public QuestionResponseDTO addQuestionToQuiz(Long quizId, QuestionRequestDTO questionDTO) {
        if (questionDTO == null) {
            throw new IllegalArgumentException("Question data must not be null.");
        }

        Quiz quiz = (Quiz) assessmentRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found with ID: " + quizId));

        Question question = null;

        switch (questionDTO.getQuestionType().toUpperCase()) {
            case "MCQ":
                if (!(questionDTO instanceof MCQRequestDTO)) {
                    throw new IllegalArgumentException("Invalid data for MCQ question.");
                }
                MCQRequestDTO mcqDTO = (MCQRequestDTO) questionDTO;
                MCQ mcq = new MCQ();
                mcq.setQuestionText(mcqDTO.getQuestionText());
                mcq.setGrade(mcqDTO.getGrade());
                mcq.setCorrectAnswer(mcqDTO.getCorrectAnswer());
                mcq.setOptions(mcqDTO.getOptions());
                mcq.setQuiz(quiz);
                question = mcq;
                break;
            case "TRUE_FALSE":
                // Handle TrueFalse question
                break;
            case "SHORT_ANSWER":
                // Handle ShortAnswer question
                break;
            default:
                throw new IllegalArgumentException("Invalid question type: " + questionDTO.getQuestionType());
        }

        questionRepository.save(question);

        return mapToQuestionResponseDTO(question);
    }

    // Utility methods for mapping entities to response DTOs
    private CourseResponseDTO mapToCourseResponseDTO(Course course) {
        CourseResponseDTO dto = new CourseResponseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setInstructorId(course.getInstructor().getId());
        return dto;
    }

    private LessonResponseDTO mapToLessonResponseDTO(Lesson lesson) {
        LessonResponseDTO dto = new LessonResponseDTO();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setCourseId(lesson.getCourse().getId());
        return dto;
    }

    private AssessmentResponseDTO mapToAssessmentResponseDTO(Assessment assessment) {
        if (assessment instanceof Quiz) {
            Quiz quiz = (Quiz) assessment;
            QuizResponseDTO quizDTO = new QuizResponseDTO();
            quizDTO.setId(quiz.getId());
            quizDTO.setTitle(quiz.getTitle());
            quizDTO.setGrade(quiz.getGrade());
            quizDTO.setType("QUIZ");
            quizDTO.setCourseId(quiz.getCourse().getId());
            quizDTO.setNumQuestions(quiz.getNumQuestions());
            return quizDTO;
        } else if (assessment instanceof Assignment) {
            Assignment assignment = (Assignment) assessment;
            AssignmentResponseDTO assignmentDTO = new AssignmentResponseDTO();
            assignmentDTO.setId(assignment.getId());
            assignmentDTO.setTitle(assignment.getTitle());
            assignmentDTO.setGrade(assignment.getGrade());
            assignmentDTO.setType("ASSIGNMENT");
            assignmentDTO.setCourseId(assignment.getCourse().getId());
            // Set additional fields if any
            return assignmentDTO;
        } else {
            throw new IllegalArgumentException("Unknown assessment type.");
        }
    }

    private QuestionResponseDTO mapToQuestionResponseDTO(Question question) {
        if (question instanceof MCQ) {
            MCQ mcq = (MCQ) question;
            MCQResponseDTO mcqDTO = new MCQResponseDTO();
            mcqDTO.setId(mcq.getId());
            mcqDTO.setQuestionText(mcq.getQuestionText());
            mcqDTO.setGrade(mcq.getGrade());
            mcqDTO.setQuestionType("MCQ");
            mcqDTO.setOptions(mcq.getOptions());
            return mcqDTO;
        }
        // Handle other question types
        else {
            throw new IllegalArgumentException("Unknown question type.");
        }
    }
}