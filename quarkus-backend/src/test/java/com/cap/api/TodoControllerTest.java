package com.cap.api;

import com.cap.PostgreSQLTestResource;
import com.cap.TestUtils;
import com.cap.api.dtos.CreateTodoDto;
import com.cap.api.dtos.RegisterUserDto;
import com.cap.api.dtos.UserCredentialsDto;
import com.cap.domain.todo.Todo;
import com.cap.domain.todo.TodoRepository;
import com.cap.domain.todo.TodoService;
import com.cap.domain.todo.TodoStatus;
import com.cap.domain.user.AuthService;
import com.cap.domain.user.User;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
class TodoControllerTest {

    @Inject
    AuthService authService;

    @Inject
    TodoService todoService;

    @Inject
    TodoRepository todoRepository;

    AbstractMap.SimpleEntry<User, Header> authenticate() {
        var login = TestUtils.generateRandomLogin();
        var dto = Map.of(
                "login", login,
                "password", "PASS"
        );


        var user = authService.registerUser(RegisterUserDto.createNew(dto.get("login"), dto.get("password"), dto.get("password")));
        var token = authService.authenticate(UserCredentialsDto.createNew(dto.get("login"), dto.get("password")));
        var header = new Header("Authorization", "Bearer " + token);

        return new AbstractMap.SimpleEntry<>(user, header);
    }

    @Test
    void shouldBlockUnauthenticatedUser() {
        given()
                .contentType(ContentType.JSON)
                .when().get("/todos")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldGetTodosList() {
        var response = given()
                .contentType(ContentType.JSON)
                .header(this.authenticate().getValue())
                .when()
                .get("/todos");

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
                .when().post("/todos")
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
    void shouldUpdateTodo() {
        var auth = this.authenticate();
        System.out.println(auth.getKey());
        var dbTodo = todoService.createTodo(auth.getKey(), CreateTodoDto.createNew("Init", TodoStatus.OPEN));
        var dto = Map.of(
                "title", "Updated",
                "status", "CLOSED"
        );
        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .header(auth.getValue())
                .when().put("/todos/" + dbTodo.id)
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
                .when().delete("/todos/" + dbTodo.id)
                .then()
                .statusCode(200);

        var deletedTodo = todoRepository.findById(auth.getKey(), dbTodo.id);
        assertTrue(deletedTodo.isEmpty());
    }

}