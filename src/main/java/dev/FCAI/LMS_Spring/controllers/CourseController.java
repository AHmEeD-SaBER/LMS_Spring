package dev.FCAI.LMS_Spring.controllers;

import dev.FCAI.LMS_Spring.entities.Course;
import dev.FCAI.LMS_Spring.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/create")
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PostMapping("/enroll/{courseId}/{userId}")
    public void enrollStudent(@PathVariable Long courseId, @PathVariable Long userId) {
        courseService.enrollStudent(courseId, userId);
    }
}
