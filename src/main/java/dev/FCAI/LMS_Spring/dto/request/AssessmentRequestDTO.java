package dev.FCAI.LMS_Spring.dto.request;

import lombok.Data;

@Data
public class AssessmentRequestDTO {
    private String title;
    private Double grade;
    private String type; // "QUIZ" or "ASSIGNMENT"
    private Long courseId;
    // Additional assessment-specific fields (e.g., numQuestions for Quiz)
}