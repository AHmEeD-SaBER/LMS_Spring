package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUsername(String username);
}
