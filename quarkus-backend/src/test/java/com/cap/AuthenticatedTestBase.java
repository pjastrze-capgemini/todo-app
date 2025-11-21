package com.cap;

import com.cap.api.dtos.RegisterUserDto;
import com.cap.api.dtos.UserCredentialsDto;
import com.cap.domain.user.AuthService;
import com.cap.domain.user.User;
import io.restassured.http.Header;
import jakarta.inject.Inject;

import java.util.AbstractMap;
import java.util.Map;

public class AuthenticatedTestBase {
    @Inject
    protected AuthService authService;

    protected AbstractMap.SimpleEntry<User, Header> authenticate() {
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
}
