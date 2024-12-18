package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.Assessment;
import dev.FCAI.LMS_Spring.entities.Course;
import dev.FCAI.LMS_Spring.entities.Instructor;
import dev.FCAI.LMS_Spring.repository.AssessmentRepository;
import dev.FCAI.LMS_Spring.repository.CourseRepository;
import dev.FCAI.LMS_Spring.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InstructorService {
    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Transactional
    public Course createCourse(Course course) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Instructor instructor = instructorRepository.findByUsername(auth.getName());
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Transactional
    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Transactional
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Transactional
    public Assessment createAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<Assessment> viewAllAssessmentsGrades(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        return courseOptional.map(Course::getAssessments).orElse(List.of());
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    public List<Course> viewAllCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Instructor instructor = instructorRepository.findByUsername(auth.getName());
        return instructor.getCreatedCourses();
    }
}
