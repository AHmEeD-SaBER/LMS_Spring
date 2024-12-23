package dev.FCAI.LMS_Spring.dto.request;

import lombok.Data;

@Data
public class CourseRequestDTO {
    private String title;
    private String description;
    private Long instructorId; // ID of the Instructor creating the course
}