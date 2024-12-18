package dev.FCAI.LMS_Spring.entities;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "role"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Student.class, name = "STUDENT"),
        @JsonSubTypes.Type(value = Instructor.class, name = "INSTRUCTOR"),
        @JsonSubTypes.Type(value = Admin.class, name = "ADMIN")
})

@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",unique = true, nullable = false)
    private String username;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true)
    @JsonBackReference
    private Admin admin;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "email",nullable = false)
    private String email;

    @Column(name= "first_name",nullable = false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "user_role",nullable = true)
    @JsonProperty("role")
    private String role;
}

