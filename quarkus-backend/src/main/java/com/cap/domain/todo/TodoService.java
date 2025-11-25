package com.cap.domain.todo;

import com.cap.api.dtos.CreateTodoDto;
import com.cap.domain.user.AuthService;
import com.cap.domain.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

import static java.util.Optional.ofNullable;

@ApplicationScoped
public class TodoService {

    @Inject
    AuthService authService;

    @Inject
    TodoRepository todoRepository;

    public List<Todo> getUsersTodos(User user) {
        return todoRepository.getUsersTodos(user);
    }

    @Transactional
    public Todo updateTodo(User user, Long todoId, CreateTodoDto dto) {
        var todo = todoRepository.findById(user, todoId).orElseThrow(() -> new NotFoundException("Todo not found"));
        todo.title = ofNullable(dto.title).orElse(todo.title);
        todo.status = ofNullable(dto.status).orElse(todo.status);
        return todoRepository.update(user, todo);
    }

    public Todo createTodo(User user, CreateTodoDto dto) {
        var todo = new Todo();
        todo.title = ofNullable(dto.title).orElse("Not title");
        todo.status = ofNullable(dto.status).orElse(TodoStatus.OPEN);
        return todoRepository.create(user, todo);
    }

    @Transactional
    public void deleteTodo(User user, Long todoId) {
        var todo = todoRepository.findById(user, todoId).orElseThrow(() -> new NotFoundException("Todo not found"));
        todoRepository.delete(user, todo);
    }
}
