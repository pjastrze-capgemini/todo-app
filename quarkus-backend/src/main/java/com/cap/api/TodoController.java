package com.cap.api;

import com.cap.api.dtos.CreateTodoDto;
import com.cap.domain.todo.Todo;
import com.cap.domain.todo.TodoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/todos")
@RolesAllowed("User")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoController {

    @Inject
    TodoService todoService;

    @GET
    @Path("")
    public List<Todo> getTodos() {
        return todoService.getUsersTodos();
    }

    @POST
    @Path("")
    public Todo createTodo(CreateTodoDto dto) {
        return todoService.createTodo(dto);
    }

    @POST
    @Path("")
    public Todo updateTodo(CreateTodoDto dto) {
        return todoService.updateTodo(dto);
    }

    @DELETE
    @Path("{todoId")
    public void deleteTodo(Long todoId) {
        todoService.deleteTodo(todoId);
    }

}