package dev.FCAI.LMS_Spring.dto.response;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}