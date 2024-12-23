// AdminService.java
package dev.FCAI.LMS_Spring.service;

import dev.FCAI.LMS_Spring.dto.*;
import dev.FCAI.LMS_Spring.dto.request.UserRequestDTO;
import dev.FCAI.LMS_Spring.dto.response.UserResponseDTO;
import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        if (userRequestDTO == null) {
            throw new IllegalArgumentException("User data must not be null.");
        }

        if (userRequestDTO.getAdminId() == null) {
            throw new IllegalArgumentException("Admin ID is required.");
        }

        Admin admin = adminRepository.findById(userRequestDTO.getAdminId())
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + userRequestDTO.getAdminId()));

        User user;
        String role = userRequestDTO.getRole().toUpperCase();

        switch (role) {
            case "STUDENT":
                user = new Student();
                break;
            case "INSTRUCTOR":
                user = new Instructor();
                break;
            case "ADMIN":
                user = new Admin();
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + userRequestDTO.getRole());
        }

        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword())); // Hash password
        user.setEmail(userRequestDTO.getEmail());
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setAdmin(admin);

        userRepository.save(user);
        return mapToUserResponseDTO(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (userRequestDTO.getUsername() != null) {
            user.setUsername(userRequestDTO.getUsername());
        }
        if (userRequestDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword())); // Hash password
        }
        if (userRequestDTO.getEmail() != null) {
            user.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getFirstName() != null) {
            user.setFirstName(userRequestDTO.getFirstName());
        }
        if (userRequestDTO.getLastName() != null) {
            user.setLastName(userRequestDTO.getLastName());
        }

        userRepository.save(user);
        return mapToUserResponseDTO(user);
    }

    public UserResponseDTO getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        return mapToUserResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers(Long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found with ID: " + adminId));

        List<User> users = admin.getUsers();

        return users.stream()
                .map(this::mapToUserResponseDTO)
                .collect(Collectors.toList());
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        return dto;
    }
}