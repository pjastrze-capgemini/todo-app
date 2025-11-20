package com.cap.api;

import com.cap.PostgreSQLTestResource;
import com.cap.api.dtos.RegisterUserDto;
import com.cap.api.dtos.UserCredentialsDto;
import com.cap.domain.user.AuthService;
import com.cap.domain.user.UserRepository;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
class AuthControllerTest {

    @Inject
    UserRepository userRepository;

    @Inject
    AuthService authService;

    String generateRandomLogin() {
        return "ADMIN_" + Math.random();
    }

    @Test
    void shouldRegisterUser() {
        var login = generateRandomLogin();
        var dto = Map.of(
                "login", login,
                "password", "PASS",
                "confirmPassword", "PASS"
        );

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/auth/register")
                .then()
                .statusCode(201)
                .body(containsString("User created"));

        var user = userRepository.getByName(login);
        assertTrue(user.isPresent(), "User not saved");
        assertNotEquals(user.get().password, dto.get("password"), "Password must be hashed");
    }

    @Test
    void shouldRegisterUserInvalidConfirmPassword() {
        var login = generateRandomLogin();
        var dto = Map.of(
                "login", login,
                "password", "PASS",
                "confirmPassword", "PASS2"
        );

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/auth/register")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldRegisterUserPreventSameUserLogin() {
        var login = generateRandomLogin();
        var dto = Map.of(
                "login", login,
                "password", "PASS",
                "confirmPassword", "PASS"
        );

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/auth/register")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/auth/register")
                .then()
                .statusCode(401);
    }


    @Test
    void shouldLoginUser() {
        var login = generateRandomLogin();
        var dto = Map.of(
                "login", login,
                "password", "PASS"
        );

        authService.registerUser(RegisterUserDto.createNew(dto.get("login"), dto.get("password"), dto.get("password")));

        var response = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/auth/login");

        response.then()
                .statusCode(200)
                .body(containsString("token"));

        assertFalse(response.jsonPath().getString("token").isEmpty());
    }

}