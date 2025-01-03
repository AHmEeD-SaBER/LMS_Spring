package dev.FCAI.LMS_Spring.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import dev.FCAI.LMS_Spring.entities.User;
import dev.FCAI.LMS_Spring.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @GetMapping("/")
    public String greet() {
        return "Hello Admin";
    }

    @PostMapping("/first-admin")
    @JsonView(Views.Summary.class)
    public ResponseEntity<?> createFirstAdmin(@RequestBody User user) {
        try {
            // Check if there are any users in the database
            if (adminService.hasUsers()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cannot create first admin: Users already exist");
            }

            if (!"ADMIN".equals(user.getRole())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("First user must be an admin");
            }

            User createdAdmin = adminService.createFirstAdmin(user);
            return ResponseEntity.ok(createdAdmin);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/user")
    @JsonView(Views.Summary.class)
    public ResponseEntity<?> createUser(@RequestBody User user) {
        // Log incoming user details
        System.out.println("Incoming User: " + user);

        try {
            // Validate role before invoking the service

            User createdUser = adminService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/users/{id}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<List<User>> findAllUsers(@PathVariable Long id){
        return ResponseEntity.ok(adminService.findAllUsers(id));
    }

    @DeleteMapping("/user/{userId}")
    @JsonView(Views.Summary.class)
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user")
    @JsonView(Views.Summary.class)
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(adminService.updateUser(user));
    }
}
