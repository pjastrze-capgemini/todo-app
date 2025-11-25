package com.cap.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@QuarkusTest
class HealthControllerTest {

    @Test
    void testHealthEndpoint() {
        given()
                .when().get("/health")
                .then()
                .statusCode(200)
                .body(containsString("ok"));
    }

    @Test
    void testSwaggerUIEndpoint() {
        given()
                .when().get("/q/swagger-ui/")
                .then()
                .statusCode(200)
                .body(containsString("<!DOCTYPE html>"));
    }

}