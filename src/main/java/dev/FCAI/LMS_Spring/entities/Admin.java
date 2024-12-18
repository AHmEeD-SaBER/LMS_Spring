package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    @OneToMany(mappedBy = "admin")
    @JsonManagedReference
    private List<User> users = new java.util.ArrayList<>();
}
