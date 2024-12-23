package dev.FCAI.LMS_Spring.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class LessonMaterialRequestDTO {
    private MultipartFile file; // Uploaded file
}
