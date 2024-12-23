package dev.FCAI.LMS_Spring.dto.response;


import lombok.Data;

@Data
public class CourseResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Long instructorId; // Only the ID of the Instructor
}
