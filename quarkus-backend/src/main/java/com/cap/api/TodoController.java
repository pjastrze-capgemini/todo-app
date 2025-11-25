package com.cap.api;

import com.cap.api.dtos.CreateTodoDto;
import com.cap.api.dtos.TodoDto;
import com.cap.domain.todo.TodoService;
import com.cap.domain.user.AuthService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ResponseStatus;

import java.util.List;
import java.util.Map;

@Path("/todo")
@RolesAllowed("User")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoController {

    private static final Logger log = Logger.getLogger(TodoController.class);

    @Inject
    AuthService authService;

    @Inject
    TodoService todoService;

    @GET
    @Path("")
    public List<TodoDto> getAllTodos() {
        var user = authService.getAuthUserOrThrow();
        log.info("GetAll: " + user.id + " " +user.name);
        return todoService.getUsersTodos(user).stream().map(TodoDto::from).toList();
    }

    @GET
    @Path("/{todoId}")
    public TodoDto getTodo(@PathParam("todoId") Long todoId) {
        var user = authService.getAuthUserOrThrow();
        return TodoDto.from(todoService.getTodo(user, todoId));
    }

    @POST
    @Path("")
    @ResponseStatus(201)
    public TodoDto createTodo(CreateTodoDto dto) {
        var user = authService.getAuthUserOrThrow();
        var todo = todoService.createTodo(user, dto);
        return TodoDto.from(todo);
    }

    @PUT
    @Path("/{todoId}")
    public TodoDto updateTodo(@PathParam("todoId") Long todoId, CreateTodoDto dto) {
        var user = authService.getAuthUserOrThrow();
        return TodoDto.from(todoService.updateTodo(user, todoId, dto));
    }

    @DELETE
    @Path("/{todoId}")
    public Map<String, String> deleteTodo(@PathParam("todoId") Long todoId) {
        var user = authService.getAuthUserOrThrow();
        todoService.deleteTodo(user, todoId);
        return Map.of("message", "Todo was removed");
    }

}