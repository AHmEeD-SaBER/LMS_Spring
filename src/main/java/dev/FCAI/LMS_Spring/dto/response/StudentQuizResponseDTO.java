package dev.FCAI.LMS_Spring.dto.response;


import lombok.Data;
import java.util.List;

@Data
public class StudentQuizResponseDTO {
    private Long quizId;
    private String title;
    private List<StudentQuestionResponseDTO> questions;
}
