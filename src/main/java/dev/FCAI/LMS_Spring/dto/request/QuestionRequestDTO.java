package dev.FCAI.LMS_Spring.dto.request;

import lombok.Data;

@Data
public abstract class QuestionRequestDTO {
    private String questionText;
    private Double grade;
    private String correctAnswer;
    private String questionType; // "MCQ", "TRUE_FALSE", or "SHORT_ANSWER"
}
