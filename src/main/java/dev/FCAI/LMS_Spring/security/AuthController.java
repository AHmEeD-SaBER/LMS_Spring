package dev.FCAI.LMS_Spring.security;

import dev.FCAI.LMS_Spring.entities.Admin;
import dev.FCAI.LMS_Spring.entities.Instructor;
import dev.FCAI.LMS_Spring.entities.Student;
import dev.FCAI.LMS_Spring.entities.User;
import dev.FCAI.LMS_Spring.repository.AdminRepository;
import dev.FCAI.LMS_Spring.repository.InstructorRepository;
import dev.FCAI.LMS_Spring.repository.StudentRepository;
import dev.FCAI.LMS_Spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

// Authentication Controller
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private InstructorRepository instructorRepository;
    
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Basic validation
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user based on type
        if (user instanceof Student) {
            studentRepository.save((Student) user);
        } else if (user instanceof Instructor) {
            instructorRepository.save((Instructor) user);
        } else if (user instanceof Admin) {
            adminRepository.save((Admin) user);
        }

        return ResponseEntity.ok("User registered successfully");
    }

    @GetMapping("/success")
    public ResponseEntity<?> loginSuccess(Authentication authentication) {
        // Determine user role and return appropriate response
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))) {
            return ResponseEntity.ok("Student logged in successfully");
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"))) {
            return ResponseEntity.ok("Instructor logged in successfully");
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok("Admin logged in successfully");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
    }
}
