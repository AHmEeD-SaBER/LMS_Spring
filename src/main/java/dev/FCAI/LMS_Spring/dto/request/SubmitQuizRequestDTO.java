package dev.FCAI.LMS_Spring.dto.request;

import lombok.Data;
import java.util.Map;

@Data
public class SubmitQuizRequestDTO {
    private Map<Long, String> answers; // Map<QuestionID, Answer>
}