package dev.FCAI.LMS_Spring.dto.request;


import lombok.Data;

@Data
public class EnrollCourseRequestDTO {
    private Long studentId;
    private Long courseId;
}