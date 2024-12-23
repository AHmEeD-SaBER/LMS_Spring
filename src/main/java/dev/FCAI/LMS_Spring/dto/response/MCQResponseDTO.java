package dev.FCAI.LMS_Spring.dto.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MCQResponseDTO extends QuestionResponseDTO {
    private List<String> options;
}
