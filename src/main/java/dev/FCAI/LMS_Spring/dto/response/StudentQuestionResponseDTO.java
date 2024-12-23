package dev.FCAI.LMS_Spring.dto.response;


import lombok.Data;
import java.util.List;

@Data
public class StudentQuestionResponseDTO {
    private Long id;
    private String questionText;
    private String questionType;

    // For MCQ questions
    private List<String> options;
}
