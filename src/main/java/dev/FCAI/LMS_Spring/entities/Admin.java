package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Setter
@Getter
@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    @OneToMany(mappedBy = "admin", orphanRemoval = true, cascade = jakarta.persistence.CascadeType.ALL)
    @JsonView(Views.Detailed.class)
    private List<User> users = new java.util.ArrayList<>();
}
