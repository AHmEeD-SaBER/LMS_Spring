package dev.FCAI.LMS_Spring.entities;

import dev.FCAI.LMS_Spring.entities.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content; // The message content of the notification

    @Column(nullable = false)
    private Boolean isRead = false; // Whether the notification has been read or not

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // The user receiving the notification

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // Timestamp of the notification

    // Constructors
    public Notification() {}

    public Notification(String content, User user) {
        this.content = content;
        this.user = user;
        this.isRead = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
