package dev.FCAI.LMS_Spring.entities;
import com.fasterxml.jackson.annotation.JsonView;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
@Setter
@Getter
@Entity
@DiscriminatorValue("ASSIGNMENT")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment extends Assessment {
    @Column(name = "submission_path")
    @JsonView(Views.Summary.class)
    private String submissionPath;

}