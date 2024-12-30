package dev.FCAI.LMS_Spring;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.AdminRepository;
import dev.FCAI.LMS_Spring.repository.UserRepository;
import dev.FCAI.LMS_Spring.service.AdminService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AdminRepository adminRepository;
    @InjectMocks
    private AdminService adminService;

    @Test
    void hasUsers_WhenUsersExist() {
        when(userRepository.count()).thenReturn(1L);
        assertTrue(adminService.hasUsers());
    }

    @Test
    void hasUsers_WhenNoUsers() {
        when(userRepository.count()).thenReturn(0L);
        assertFalse(adminService.hasUsers());
    }

    @Test
    void createFirstAdmin_Success() {
        Admin admin = new Admin();
        admin.setUsername("admin1");
        admin.setPassword("password");
        admin.setEmail("admin@test.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");

        when(userRepository.count()).thenReturn(0L);
        when(userRepository.save(any(Admin.class))).thenReturn(admin);

        User result = adminService.createFirstAdmin(admin);
        assertEquals(admin, result);
    }

    @Test
    void createFirstAdmin_WhenUsersExist() {
        when(userRepository.count()).thenReturn(1L);
        assertThrows(IllegalStateException.class, () ->
                adminService.createFirstAdmin(new Admin())
        );
    }

    @Test
    void createUser_Success() {
        // Create admin
        Admin admin = new Admin();
        admin.setId(1L);
        admin.setUsers(new ArrayList<>());

        // Create student
        Student student = new Student();
        student.setUsername("student1");
        student.setPassword("password");
        student.setEmail("student@test.com");
        student.setFirstName("Student");
        student.setLastName("User");
        student.setAdmin(admin);

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.save(any(User.class))).thenReturn(student);

        User result = adminService.createUser(student);
        assertEquals(student, result);
        assertEquals("STUDENT", result.getRole());
    }

    @Test
    void createUser_NullUser() {
        assertThrows(IllegalArgumentException.class, () ->
                adminService.createUser(null)
        );
    }

    @Test
    void createUser_NoAdmin() {
        Student student = new Student();
        student.setUsername("student1");
        student.setPassword("password");
        student.setEmail("student@test.com");
        student.setFirstName("Student");
        student.setLastName("User");

        assertThrows(IllegalArgumentException.class, () ->
                adminService.createUser(student)
        );
    }

    @Test
    void createUser_AdminNotFound() {
        Admin admin = new Admin();
        admin.setId(1L);

        Student student = new Student();
        student.setUsername("student1");
        student.setPassword("password");
        student.setEmail("student@test.com");
        student.setFirstName("Student");
        student.setLastName("User");
        student.setAdmin(admin);

        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                adminService.createUser(student)
        );
    }

    @Test
    void deleteUser_Success() {
        Student student = new Student();
        student.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(student));

        assertDoesNotThrow(() -> adminService.deleteUser(1L));
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () ->
                adminService.deleteUser(1L)
        );
    }

    @Test
    void updateUser_Success() {
        Student student = new Student();
        student.setId(1L);
        student.setUsername("student1");
        student.setEmail("updated@test.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(student));
        when(userRepository.save(any(User.class))).thenReturn(student);

        User result = adminService.updateUser(student);
        assertEquals(student, result);
        assertEquals("STUDENT", result.getRole());
    }

    @Test
    void updateUser_UserNotFound() {
        Student student = new Student();
        student.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                adminService.updateUser(student)
        );
    }

    @Test
    void findAllUsers_Success() {
        Admin admin = new Admin();
        admin.setId(1L);

        List<User> users = Arrays.asList(
                createStudent("student1", admin),
                createStudent("student2", admin)
        );
        admin.setUsers(users);

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        List<User> result = adminService.findAllUsers(1L);
        assertEquals(users, result);
        assertEquals(2, result.size());
    }

    @Test
    void findAllUsers_AdminNotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                adminService.findAllUsers(1L)
        );
    }

    // Helper method to create test students
    private Student createStudent(String username, Admin admin) {
        Student student = new Student();
        student.setUsername(username);
        student.setPassword("password");
        student.setEmail(username + "@test.com");
        student.setFirstName("Test");
        student.setLastName("Student");
        student.setAdmin(admin);
        return student;
    }
}