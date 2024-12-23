package dev.FCAI.LMS_Spring.dto.response;

import lombok.Data;

@Data
public class LessonMaterialResponseDTO {
    private Long id;
    private String filename;
    private String fileType;
    private String fileUrl; // URL to access/download the file
}