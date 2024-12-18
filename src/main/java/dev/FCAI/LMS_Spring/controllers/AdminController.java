//package dev.FCAI.LMS_Spring.controllers;
//
//import dev.FCAI.LMS_Spring.entities.Admin;
//import dev.FCAI.LMS_Spring.entities.Student;
//import dev.FCAI.LMS_Spring.entities.User;
//import dev.FCAI.LMS_Spring.service.AdminService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin")
//public class AdminController {
//    @Autowired
//    private AdminService adminService;
//
//    @PostMapping("/user")
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        try {
//            User createdUser = adminService.createUser(user);  // Service handles Admin assignment logic
//            return ResponseEntity.ok(createdUser);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//
////    @GetMapping("/users")
////    public ResponseEntity<List<User>> findAllusers(){
////        return ResponseEntity.ok(adminService.findAllusers());
////    }
//
//    @PostMapping("/user/{userId}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
//        adminService.deleteUser(userId);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/user")
//    public ResponseEntity<User> updateUser(@RequestBody User user) {
//        return ResponseEntity.ok(adminService.updateUser(user));
//    }
//}
