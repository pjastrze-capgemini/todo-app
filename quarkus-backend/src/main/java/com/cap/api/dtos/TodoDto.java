package com.cap.api.dtos;

import com.cap.domain.todo.Todo;
import com.cap.domain.todo.TodoStatus;

public class TodoDto {
    public Long id;
    public String title;
    public TodoStatus status;
    public UserDto owner;

    public static TodoDto from(Todo todo) {
        var dto = new TodoDto();
        dto.id = todo.id;
        dto.title = todo.title;
        dto.status = todo.status;
        dto.owner = UserDto.from(todo.owner);
        return dto;
    }
}
