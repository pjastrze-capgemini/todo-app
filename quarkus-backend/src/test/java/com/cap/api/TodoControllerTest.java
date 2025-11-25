package com.cap.api;

import com.cap.AuthenticatedTestBase;
import com.cap.PostgreSQLTestResource;
import com.cap.api.dtos.CreateTodoDto;
import com.cap.api.dtos.TodoDto;
import com.cap.domain.todo.Todo;
import com.cap.domain.todo.TodoRepository;
import com.cap.domain.todo.TodoService;
import com.cap.domain.todo.TodoStatus;
import com.cap.domain.user.AuthService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
class TodoControllerTest extends AuthenticatedTestBase {

    @Inject
    AuthService authService;

    @Inject
    TodoService todoService;

    @Inject
    TodoRepository todoRepository;

    @Test
    void shouldBlockUnauthenticatedUser() {
        given()
                .contentType(ContentType.JSON)
                .when().get("/todo")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldGetTodosList() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(this.authenticate().getValue())
                .when()
                .get("/todo");

        response.then().statusCode(200);
        assertTrue(response.body().asString().contains("["));
    }

    @Test
    void shouldCreateTodo() {
        var auth = this.authenticate();
        var dto = Map.of(
                "title", "New Todo",
                "status", "OPEN"
        );
        var newTodo = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .header(auth.getValue())
                .when().post("/todo")
                .then()
                .statusCode(201)
                .extract()
                .body()
                .as(Todo.class);

        var savedTodoOptional = todoRepository.findById(auth.getKey(), newTodo.id);
        assertTrue(savedTodoOptional.isPresent());
        var savedTodo = savedTodoOptional.get();
        assertEquals("New Todo", savedTodo.title);
        assertEquals(TodoStatus.OPEN, savedTodo.status);

    }

    @Test
    void shouldGetTodoById() {
        var auth = this.authenticate();
        var dbTodo = todoService.createTodo(auth.getKey(), CreateTodoDto.createNew("Init", TodoStatus.OPEN));

        var todoDto = given()
                .header(auth.getValue())
                .when().get("/todo/" + dbTodo.id)
                .then()
                .statusCode(200)
                .extract()
                .as(TodoDto.class);

        assertEquals(dbTodo.title, todoDto.title);
        assertEquals(dbTodo.status, todoDto.status);
        assertEquals(dbTodo.owner.id, todoDto.owner.id);
    }

    @Test
    void shouldUpdateTodo() {
        var auth = this.authenticate();
        var dbTodo = todoService.createTodo(auth.getKey(), CreateTodoDto.createNew("Init", TodoStatus.OPEN));
        var dto = Map.of(
                "title", "Updated",
                "status", "CLOSED"
        );
        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .header(auth.getValue())
                .when().put("/todo/" + dbTodo.id)
                .then()
                .statusCode(200);

        var savedTodoOptional = todoRepository.findById(auth.getKey(), dbTodo.id);
        assertTrue(savedTodoOptional.isPresent());
        var savedTodo = savedTodoOptional.get();
        assertEquals("Updated", savedTodo.title);
        assertEquals(TodoStatus.CLOSED, savedTodo.status);
    }

    @Test
    void shouldDeleteTodo() {
        var auth = this.authenticate();
        var dbTodo = todoService.createTodo(auth.getKey(), CreateTodoDto.createNew("Init", TodoStatus.OPEN));

        given()
                .header(auth.getValue())
                .when().delete("/todo/" + dbTodo.id)
                .then()
                .statusCode(200);

        var deletedTodo = todoRepository.findById(auth.getKey(), dbTodo.id);
        assertTrue(deletedTodo.isEmpty());
    }

}