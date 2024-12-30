package dev.FCAI.LMS_Spring.entities;
import jakarta.persistence.*;
import lombok.*;
@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@DiscriminatorValue("QUIZ")
@AllArgsConstructor
public class Quiz extends Assessment {
    private int numberOfQuestionsToAssign;
}
