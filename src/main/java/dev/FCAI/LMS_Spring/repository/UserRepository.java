package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
