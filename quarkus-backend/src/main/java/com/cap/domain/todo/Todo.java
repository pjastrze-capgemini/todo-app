package com.cap.domain.todo;

import com.cap.domain.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "\"Todos\"")
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    public User owner;
    public String title;
    @Enumerated(EnumType.STRING)
    public TodoStatus status;
    @Column(name = "created_at")
    public Instant createAt;
}