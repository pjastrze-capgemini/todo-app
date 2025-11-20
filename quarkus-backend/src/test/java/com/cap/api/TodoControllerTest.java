package com.cap.api;

import com.cap.PostgreSQLTestResource;
import com.cap.TestUtils;
import com.cap.api.dtos.RegisterUserDto;
import com.cap.api.dtos.UserCredentialsDto;
import com.cap.domain.todo.Todo;
import com.cap.domain.user.AuthService;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
class TodoControllerTest {

    @Inject
    AuthService authService;

    Header authenticate() {
        var login = TestUtils.generateRandomLogin();
        var dto = Map.of(
                "login", login,
                "password", "PASS"
        );

        authService.registerUser(RegisterUserDto.createNew(dto.get("login"), dto.get("password"), dto.get("password")));
        var token = authService.authenticate(UserCredentialsDto.createNew(dto.get("login"), dto.get("password")));
        return new Header("Authorization", "Bearer " + token);
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
                .header(this.authenticate())
                .when()
                .get("/todos");

        response.then().statusCode(200);
        assertEquals(response.body().asString(), "[]");
    }

    @Test
    void shouldCreateTodo() {
        throw new NotImplementedException("TODO");
    }

    @Test
    void shouldUpdateTodo() {
        throw new NotImplementedException("TODO");
    }

    @Test
    void shouldDeleteTodo() {
        throw new NotImplementedException("TODO");
    }

}