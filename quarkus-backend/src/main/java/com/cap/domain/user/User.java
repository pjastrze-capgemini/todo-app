package com.cap.domain.user;

import com.cap.domain.todo.Todo;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "\"Users\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String name;
    public String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Todo> todos;

}