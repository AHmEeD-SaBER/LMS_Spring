package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.entities.Course;
import dev.FCAI.LMS_Spring.repository.CourseRepository;
import dev.FCAI.LMS_Spring.event.CourseEnrollmentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CourseService(CourseRepository courseRepository, ApplicationEventPublisher eventPublisher) {
        this.courseRepository = courseRepository;
        this.eventPublisher = eventPublisher;
    }

    public Course createCourse(Course course) {
        Course createdCourse = courseRepository.save(course);
        return createdCourse;
    }

    public void enrollStudent(Long courseId, Long userId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        // Add logic to enroll the user
        eventPublisher.publishEvent(new CourseEnrollmentEvent(this, course));
    }
}
