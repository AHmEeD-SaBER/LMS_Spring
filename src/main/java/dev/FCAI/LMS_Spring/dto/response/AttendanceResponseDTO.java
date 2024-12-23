package dev.FCAI.LMS_Spring.dto.response;


import lombok.Data;

@Data
public class AttendanceResponseDTO {
    private Long lessonId;
    private Long studentId;
    private String message;
}
