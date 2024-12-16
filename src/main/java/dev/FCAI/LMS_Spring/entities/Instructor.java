package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@DiscriminatorValue("INSTRUCTOR")
public class Instructor extends User {
    @OneToMany(mappedBy = "instructor")
    private List<Course> createdCourses;

    @Override
    public Set<String> getRoles() {
        return Set.of("ROLE_INSTRUCTOR");
    }

}
