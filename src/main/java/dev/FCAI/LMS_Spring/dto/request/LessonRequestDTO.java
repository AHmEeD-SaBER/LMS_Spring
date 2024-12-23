package dev.FCAI.LMS_Spring.dto.request;


import lombok.Data;

@Data
public class LessonRequestDTO {
    private String title;
    private Long courseId; // ID of the Course to which the lesson belongs
}