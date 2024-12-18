package dev.FCAI.LMS_Spring.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.Set;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

}
