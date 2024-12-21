package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.*;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Setter
@Getter
@Entity
@DiscriminatorValue("INSTRUCTOR")
public class Instructor extends User {
    @OneToMany(mappedBy = "instructor")
    @JsonView(Views.Detailed.class)
    private List<Course> createdCourses = new ArrayList<>();
}