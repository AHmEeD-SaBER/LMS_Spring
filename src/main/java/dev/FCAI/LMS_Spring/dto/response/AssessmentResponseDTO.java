package dev.FCAI.LMS_Spring.dto.response;

import lombok.Data;

@Data
public class AssessmentResponseDTO {
    private Long id;
    private String title;
    private Double grade;
    private String type;
    private Long courseId;
}