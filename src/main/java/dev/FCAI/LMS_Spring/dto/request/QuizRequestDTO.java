package dev.FCAI.LMS_Spring.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizRequestDTO extends AssessmentRequestDTO {
    private Long courseId;
    private Integer numQuestions;
}
