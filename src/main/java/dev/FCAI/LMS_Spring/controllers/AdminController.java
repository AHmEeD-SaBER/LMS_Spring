
package dev.FCAI.LMS_Spring.controllers;

import dev.FCAI.LMS_Spring.dto.*;
import dev.FCAI.LMS_Spring.dto.request.UserRequestDTO;
import dev.FCAI.LMS_Spring.dto.response.UserResponseDTO;
import dev.FCAI.LMS_Spring.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/user")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponse = adminService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponse = adminService.updateUser(userId, userRequestDTO);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long userId) {
        UserResponseDTO userResponse = adminService.getUser(userId);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/users/{adminId}")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@PathVariable Long adminId) {
        List<UserResponseDTO> users = adminService.getAllUsers(adminId);
        return ResponseEntity.ok(users);
    }
}