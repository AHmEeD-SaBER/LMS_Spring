package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.Assessment;
import dev.FCAI.LMS_Spring.entities.Course;
import dev.FCAI.LMS_Spring.entities.Instructor;
import dev.FCAI.LMS_Spring.repository.AssessmentRepository;
import dev.FCAI.LMS_Spring.repository.CourseRepository;
import dev.FCAI.LMS_Spring.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @Transactional
//    public Course createCourse(Course course) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Instructor instructor = instructorRepository.findByUsername(auth.getName());
//        course.setInstructor(instructor);
//        return courseRepository.save(course);
//    }


    @Transactional
    public void deleteCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    @Transactional
    public Course updateCourse(Course course) {
        return courseRepository.save(course);
    }

    @Transactional
    public Assessment createAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    public List<Assessment> viewAllAssessmentsGrades(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        return courseOptional.map(Course::getAssessments).orElse(List.of());
    }
//    public List<Course> viewAllCourses() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        Instructor instructor = instructorRepository.findByUsername(auth.getName());
//        return instructor.getCreatedCourses();
//    }
}
