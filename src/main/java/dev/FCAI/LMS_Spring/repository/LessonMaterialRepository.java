package dev.FCAI.LMS_Spring.repository;

import dev.FCAI.LMS_Spring.entities.LessonMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonMaterialRepository extends JpaRepository<LessonMaterial, Long> {
    List<LessonMaterial> findByLessonId(Long lessonId);
}
