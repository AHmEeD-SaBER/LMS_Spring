package dev.FCAI.LMS_Spring;

import dev.FCAI.LMS_Spring.entities.*;
import dev.FCAI.LMS_Spring.repository.AdminRepository;
import dev.FCAI.LMS_Spring.repository.UserRepository;
import dev.FCAI.LMS_Spring.service.AdminService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AdminRepository adminRepository;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId(1L);
        admin.setUsername("admin_user");
        admin.setPassword("adminpass");
        admin.setEmail("admin@example.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setUsers(new ArrayList<>());
    }


    @Test
    void testCreateUser_Success() {
        Student student = new Student();
        student.setUsername("student1");
        student.setPassword("password");
        student.setEmail("student1@example.com");
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setAdmin(admin);
        student.setEnrolledCourses(new ArrayList<>());
        student.setNotifications(new ArrayList<>());
        student.setSubmissions(new ArrayList<>());
        student.setAttendedLessons(new ArrayList<>());
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.save(student)).thenAnswer(invocation -> {
            Student savedStudent = invocation.getArgument(0);
            savedStudent.setId(2L); // Simulate auto-generated ID
            return savedStudent;
        });

        User result = adminService.createUser(student);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertTrue(admin.getUsers().contains(result));

        verify(adminRepository).findById(1L);
        verify(adminRepository).save(admin);
        verify(userRepository).save(student);
    }

    @Test
    void testCreateUser_NullUser() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.createUser(null);
        });
        assertEquals("User object cannot be null.", exception.getMessage());
    }


    @Test
    void testCreateUser_AdminNotProvided() {
        Instructor instructor = new Instructor();
        instructor.setUsername("instructor1");
        instructor.setPassword("password");
        instructor.setEmail("instructor1@example.com");
        instructor.setFirstName("Jack");
        instructor.setLastName("Smith");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.createUser(instructor);
        });
        assertEquals("Admin must be provided and cannot be null.", exception.getMessage());
    }

    @Test
    void testCreateUser_AdminNotFound() {
        Student student = new Student();
        student.setUsername("student3");
        student.setPassword("password");
        student.setEmail("student3@example.com");
        student.setFirstName("Alice");
        student.setLastName("Brown");
        Admin nonExistentAdmin = new Admin();
        nonExistentAdmin.setId(99L);
        student.setAdmin(nonExistentAdmin);

        when(adminRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.createUser(student);
        });
        assertEquals("Admin with id 99 does not exist.", exception.getMessage());
    }

    @Test
    void testCreateUser_UserAlreadyExists() {
        Student existingStudent = new Student();
        existingStudent.setId(2L);
        existingStudent.setUsername("student1");
        existingStudent.setPassword("password");
        existingStudent.setEmail("student1@example.com");
        existingStudent.setFirstName("John");
        existingStudent.setLastName("Doe");
        existingStudent.setAdmin(admin);
        admin.getUsers().add(existingStudent);
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.createUser(existingStudent);
        });
        assertEquals("User already exists.", exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(new Student()));

        adminService.deleteUser(2L);

        verify(userRepository).findById(2L);
        verify(userRepository).deleteById(2L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.deleteUser(2L);
        });
        assertEquals("User not found.", exception.getMessage());
    }


    @Test
    void testUpdateUser_Success() {
        Student existingStudent = new Student();
        existingStudent.setId(2L);
        existingStudent.setUsername("student1");
        existingStudent.setPassword("password");
        existingStudent.setEmail("student1@example.com");
        existingStudent.setFirstName("John");
        existingStudent.setLastName("Doe");
        existingStudent.setEmail("newemail@example.com");
        existingStudent.setPassword("newpassword");

        when(userRepository.findById(2L)).thenReturn(Optional.of(existingStudent));
        when(userRepository.save(existingStudent)).thenReturn(existingStudent);

        User updatedUser = adminService.updateUser(existingStudent);

        assertNotNull(updatedUser);
        assertEquals("newemail@example.com", updatedUser.getEmail());
        assertEquals("newpassword", updatedUser.getPassword());

        verify(userRepository).findById(2L);
        verify(userRepository).save(existingStudent);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        Student student = new Student();
        student.setId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.updateUser(student);
        });
        assertEquals("User not found", exception.getMessage());
    }


    @Test
    void testFindAllUsers_Success() {
        Student student1 = new Student();
        student1.setId(2L);
        student1.setUsername("student1");

        Instructor instructor1 = new Instructor();
        instructor1.setId(3L);
        instructor1.setUsername("instructor1");

        admin.getUsers().add(student1);
        admin.getUsers().add(instructor1);

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        List<User> users = adminService.findAllUsers(1L);

        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains(student1));
        assertTrue(users.contains(instructor1));

        verify(adminRepository).findById(1L);
    }

    @Test
    void testFindAllUsers_AdminNotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.findAllUsers(1L);
        });
        assertEquals("Admin not found", exception.getMessage());
    }
}