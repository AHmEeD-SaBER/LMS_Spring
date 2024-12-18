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

    @Transactional
    public Course createCourse(Course course, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new RuntimeException("Instructor not found"));
        if(instructor.getCreatedCourses().contains(course)) return course;
        instructor.getCreatedCourses().add(course);
        instructorRepository.save(instructor);
        course.setInstructor(instructor);
        return courseRepository.save(course);
    }


    @Transactional
    public void deleteCourse(Long courseId, Long instructorId) {
        if(courseRepository.findById(courseId).isEmpty()) return;
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new RuntimeException("Instructor not found"));
        if(instructor.getCreatedCourses().contains(courseRepository.findById(courseId).get())) {
            courseRepository.deleteById(courseId);
        }
        else throw new RuntimeException("Course not found");
    }

    @Transactional
    public Course updateCourse(Course course, Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new RuntimeException("Instructor not found"));
        if(courseRepository.findById(course.getId()).isEmpty()) throw new RuntimeException("Course not found");
        if(instructor.getCreatedCourses().contains(course)) {
            return courseRepository.save(course);
        }
        else throw new RuntimeException("Course not found");
    }

    @Transactional
    public Assessment createAssessment(Assessment assessment) {
        return assessmentRepository.save(assessment);
    }

    public List<Assessment> viewAllAssessmentsGrades(Long courseId) {
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        return courseOptional.map(Course::getAssessments).orElse(List.of());
    }
    public List<Course> viewAllCourses(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElseThrow(() -> new RuntimeException("Instructor not found"));
        return instructor.getCreatedCourses();
    }
}
