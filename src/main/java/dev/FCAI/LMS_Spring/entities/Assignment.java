package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@DiscriminatorValue("ASSIGNMENT")
public class Assignment extends Assessment {
    @Column(nullable = false)
    private String submissionPath;

}
