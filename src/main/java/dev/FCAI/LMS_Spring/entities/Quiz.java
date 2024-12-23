package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Builder
@DiscriminatorValue("QUIZ")
@AllArgsConstructor
public class Quiz extends Assessment {

}
