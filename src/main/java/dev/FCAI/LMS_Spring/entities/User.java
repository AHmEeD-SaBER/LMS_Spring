package dev.FCAI.LMS_Spring.entities;
import com.fasterxml.jackson.annotation.*;
import dev.FCAI.LMS_Spring.Views;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_role")
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Summary.class)
    private Long id;


    @Column(name = "username",unique = true, nullable = false)
    @JsonView(Views.Summary.class)
    private String username;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = true)
    @JsonView(Views.Summary.class)
    private Admin admin;

    @Column(name = "password",nullable = false)
    @JsonView(Views.Detailed.class)
    private String password;

    @Column(name = "email",nullable = false)
    @JsonView(Views.Detailed.class)
    private String email;

    @Column(name= "first_name",nullable = false)
    @JsonView(Views.Detailed.class)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    @JsonView(Views.Detailed.class)
    private String lastName;

    @Transient
    @JsonProperty("role")
    public String getRole() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }
}

