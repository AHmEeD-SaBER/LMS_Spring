package dev.FCAI.LMS_Spring.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@DiscriminatorValue("STUDENT")
public class Student extends User {
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonManagedReference
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> enrolledCourses = new java.util.ArrayList<>();

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonManagedReference
    @JoinTable(
            name = "student_notifications",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "notification_id")
    )
    private List<Notification> notifications = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Assessment> submittedAssessments = new java.util.ArrayList<>();


}
