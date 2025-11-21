package com.cap.api;

import com.cap.api.dtos.CreateTodoDto;
import com.cap.domain.todo.Todo;
import com.cap.domain.todo.TodoService;
import com.cap.domain.user.AuthService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/todos")
@RolesAllowed("User")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoController {

    @Inject
    AuthService authService;

    @Inject
    TodoService todoService;

    @GET
    @Path("")
    public List<Todo> getTodos() {
        var user = authService.getAuthUserOrThrow();
        return todoService.getUsersTodos(user);
    }

    @POST
    @Path("")
    public Response createTodo(CreateTodoDto dto) {
        var user = authService.getAuthUserOrThrow();
        var todo = todoService.createTodo(user, dto);
        return Response.ok(todo).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{todoId}")
    public Todo updateTodo(@PathParam("todoId") Long todoId, CreateTodoDto dto) {
        var user = authService.getAuthUserOrThrow();
        return todoService.updateTodo(user, todoId, dto);
    }

    @DELETE
    @Path("/{todoId}")
    public Response deleteTodo(@PathParam("todoId") Long todoId) {
        var user = authService.getAuthUserOrThrow();
        todoService.deleteTodo(user, todoId);
        return Response
                .ok(Map.of("message", "Response was removed"))
                .build();
    }

}