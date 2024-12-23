package dev.FCAI.LMS_Spring.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuizResponseDTO extends AssessmentResponseDTO {
    private Integer numQuestions;
    private List<QuestionResponseDTO> questions;
}
