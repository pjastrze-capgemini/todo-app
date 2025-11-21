package com.cap.api;

import com.cap.AuthenticatedTestBase;
import com.cap.PostgreSQLTestResource;
import com.cap.api.dtos.UserDto;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(PostgreSQLTestResource.class)
class AuthControllerAuthenticatedTest extends AuthenticatedTestBase {

    @Test
    void shouldGetLoggedUserInfo() {
        var auth = this.authenticate();
        var userDto = given()
                .contentType(ContentType.JSON)
                .header(auth.getValue())
                .when().get("/auth/me")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(UserDto.class);

        assertEquals(auth.getKey().id, userDto.id);
        assertEquals(auth.getKey().name, userDto.name);
    }

    @Test
    void shouldNotGetLoggedUserInfoForUnauthenticatedUser() {
        given()
                .contentType(ContentType.JSON)
                .when().get("/auth/me")
                .then()
                .statusCode(401);
    }

    @Test
    void shouldNotGetLoggedUserInfoWhenInvalidToken() {
        given()
                .contentType(ContentType.JSON)
                .header(new Header("Authorization", "Bearer 34234342_invalid_token"))
                .when().get("/auth/me")
                .then()
                .statusCode(401);
    }

}