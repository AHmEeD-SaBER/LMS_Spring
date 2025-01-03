package dev.FCAI.LMS_Spring.entities;
import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String filename;
    @Lob
    @Column(nullable = false)
    private byte[] data;
    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
}