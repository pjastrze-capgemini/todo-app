package com.cap.api.dtos;

import com.cap.domain.todo.TodoStatus;

public class CreateTodoDto {
    public Long id;
    public String title;
    public TodoStatus status;

    public static CreateTodoDto createNew(String title, TodoStatus status) {
        var dto = new CreateTodoDto();
        dto.title = title;
        dto.status = status;
        return dto;
    }
}
