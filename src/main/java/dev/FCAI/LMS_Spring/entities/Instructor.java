package dev.FCAI.LMS_Spring.entities;
import com.fasterxml.jackson.annotation.*;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.*;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Instructor extends User {
    @OneToMany(mappedBy = "instructor")
    @JsonView(Views.Detailed.class)
    private List<Course> createdCourses = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "instructor_notifications",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id")
    )
    @JsonView(Views.Detailed.class)
    private List<Notification> notifications = new java.util.ArrayList<>();
}