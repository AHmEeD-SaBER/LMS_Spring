package dev.FCAI.LMS_Spring.dto.response;


import lombok.Data;

@Data
public abstract class QuestionResponseDTO {
    private Long id;
    private String questionText;
    private Double grade;
    private String questionType;
}