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
public class InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private AssessmentRepository assessmentRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private NotificationsService notificationPublisher;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public Course createCourse(Course course, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        instructor.getCreatedCourses().add(course);
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    @Transactional
    public Assessment createAssessment(Assessment assessment, Long courseId, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!instructor.getCreatedCourses().contains(course)) {
            throw new RuntimeException("Instructor does not own the course");
        }

        assessment.setCourse(course);
        course.getAssessments().add(assessment);

        // Save the assessment first
        Assessment savedAssessment = assessmentRepository.save(assessment);

        // Save the questions associated with the assessment
        for (Question question : savedAssessment.getQuestions()) {
            question.setAssessment(savedAssessment);
        }

        List<Student> enrolledStudents = course.getEnrolledStudents();
        for (Student student : enrolledStudents) {
            notificationPublisher.notifyStudent(student,
                    "New Assessment Published: " + course.getTitle() + " - " + assessment.getTitle());
        }

        courseRepository.save(course);
        return savedAssessment;
    }


    @Transactional
    public Lesson createLesson(Lesson lesson, Long courseId, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!instructor.getCreatedCourses().contains(course)) {
            throw new RuntimeException("Instructor does not own this course");
        }

        lesson.setCourse(course);
        course.getLessons().add(lesson);

        List<Student> enrolledStudents = course.getEnrolledStudents();
        for (Student student : enrolledStudents) {
            notificationPublisher.notifyStudent(student,
                    "New Lesson Added: " + course.getTitle() + " - " + lesson.getTitle());
        }

        lessonRepository.save(lesson);
        return lesson;
    }

    @Transactional
    public List<Course> viewAllCourses(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        return instructor.getCreatedCourses();
    }

    @Transactional
    public Course updateCourse(Course course, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        if (!instructor.getCreatedCourses().contains(course)) {
            course.setInstructor(instructor);
            return courseRepository.save(course);
        }
        throw new RuntimeException("Instructor does not own this course");
    }

    @Transactional
    public void deleteCourse(Long courseId, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        if (instructor.getCreatedCourses().contains(course)) {
            courseRepository.delete(course);
        } else {
            throw new RuntimeException("Instructor does not own this course");
        }
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

    @Transactional
    public Lesson uploadLessonMaterial(Long instructorId, Long lessonId, MultipartFile file) throws IOException {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        Course course = lesson.getCourse();

        if (!instructor.getCreatedCourses().contains(course)) {
            throw new RuntimeException("Instructor does not own this course");
        }

        // Create a new LessonMaterial
        LessonMaterial lessonMaterial = new LessonMaterial();

        // Include lesson ID in filename
        lessonMaterial.setFilename("lesson_" + lessonId + "_" + file.getOriginalFilename());
        lessonMaterial.setData(file.getBytes());
        lessonMaterial.setLesson(lesson);

        lesson.getMaterials().add(lessonMaterial);

        lessonRepository.save(lesson);

        return lesson;
    }

    public AssignmentSubmission gradeAssignment(Long submissionId, double grade, String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        if (!(submission instanceof AssignmentSubmission)) {
            throw new RuntimeException("Submission is not an assignment submission");
        }
        Assignment assignment = (Assignment) submission.getAssessment();
        if (assignment.getGrade() != null && (grade < 0 || grade > assignment.getGrade())) {
            throw new RuntimeException("Grade must be between 0 and " + assignment.getGrade());
        }


        AssignmentSubmission assignmentSubmission = (AssignmentSubmission) submission;
        assignmentSubmission.setTotalScore(grade);
        assignmentSubmission.setFeedback(feedback);
        assignmentSubmission.setGraded(true);

        List<Student> enrolledStudents = assignmentSubmission.getAssessment().getCourse().getEnrolledStudents();
        for (Student student : enrolledStudents) {
            notificationPublisher.notifyStudent(student,
                    "Assignment Graded: " + assignmentSubmission.getAssessment().getCourse().getTitle() + " - " + assignmentSubmission.getAssessment().getTitle());
        }
        AssignmentSubmission savedSubmission = (AssignmentSubmission) submissionRepository.save(assignmentSubmission);
        Student student = savedSubmission.getStudent();
        student.getSubmissions().add(savedSubmission);
        studentRepository.save(student);
        return savedSubmission;
    }

    public List<Submission> getAssessmentSubmissions(Long assessmentId) {
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));

        return submissionRepository.findByAssessment(assessment);
    }

    public List<Notification> getNotifications(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new RuntimeException("Instructor not found"));
        return instructor.getNotifications();
    }


}