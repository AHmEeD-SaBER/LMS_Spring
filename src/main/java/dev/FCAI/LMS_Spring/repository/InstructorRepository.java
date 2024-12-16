package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
    Instructor findByUsername(String username);
}
