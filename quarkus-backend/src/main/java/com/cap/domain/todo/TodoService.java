package com.cap.domain.todo;

import com.cap.api.dtos.CreateTodoDto;
import com.cap.domain.user.AuthService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;

import java.util.List;

@ApplicationScoped
public class TodoService {

    @Inject
    AuthService authService;

    @Inject
    TodoRepository todoRepository;

    public List<Todo> getUsersTodos() {
        var user = authService.getAuthUser().orElseThrow(() -> new NotAuthorizedException("Unauthorized"));
        return todoRepository.getUsersTodos(user);
    }

    public Todo createTodo(CreateTodoDto dto) {
        var user = authService.getAuthUser().orElseThrow(() -> new NotAuthorizedException("Unauthorized"));
        var todo = new Todo();
        todo.title = dto.title;
        todo.status = dto.status;
        return todoRepository.create(user, todo);
    }

    public Todo updateTodo(CreateTodoDto dto) {
        var user = authService.getAuthUser().orElseThrow(() -> new NotAuthorizedException("Unauthorized"));
        var todo = todoRepository.findById(user, dto.id).orElseThrow(() -> new NotFoundException("Todo not found"));
        todo.title = dto.title;
        todo.status = dto.status;
        return todoRepository.update(user, todo);
    }

    public void deleteTodo(Long todoId) {
        var user = authService.getAuthUser().orElseThrow(() -> new NotAuthorizedException("Unauthorized"));
        var todo = todoRepository.findById(user, todoId).orElseThrow(() -> new NotFoundException("Todo not found"));
        todoRepository.delete(user, todo);
    }
}
