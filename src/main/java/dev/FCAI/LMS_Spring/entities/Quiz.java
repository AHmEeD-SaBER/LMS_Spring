package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("QUIZ")
public class Quiz extends Assessment {

}
