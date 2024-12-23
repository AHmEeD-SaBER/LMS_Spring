package dev.FCAI.LMS_Spring.dto.response;

import lombok.Data;

@Data
public class LessonResponseDTO {
    private Long id;
    private String title;
    private Long courseId;
}
