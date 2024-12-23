package dev.FCAI.LMS_Spring.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MCQRequestDTO extends QuestionRequestDTO {
    private List<String> options;
}
