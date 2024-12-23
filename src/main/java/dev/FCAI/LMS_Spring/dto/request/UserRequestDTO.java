package dev.FCAI.LMS_Spring.dto.request;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String role; // "STUDENT", "INSTRUCTOR", or "ADMIN"
    private Long adminId; // The ID of the Admin creating the user
}