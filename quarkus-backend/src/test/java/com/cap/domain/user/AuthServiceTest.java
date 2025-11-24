package com.cap.domain.user;

import com.cap.api.dtos.UserCredentialsDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuthServiceTest {

    @InjectMock
    protected UserRepository userRepository;

    @Inject
    protected AuthService authService;

    @Test
    void shouldAuthenticateUser() {
        var mockUser = new User();
        mockUser.name = "test";
        mockUser.password = PasswordUtil.hashPassword("password");
        when(userRepository.getByName(anyString())).thenReturn(Optional.of(mockUser));

        var token = authService.authenticate(UserCredentialsDto.createNew("test", "password"));
        assertTrue(token.length() > 10);
    }

    @Test
    void shouldNotAuthenticateUser() {
        var mockUser = new User();
        mockUser.name = "test";
        mockUser.password = PasswordUtil.hashPassword("password");
        when(userRepository.getByName(anyString())).thenReturn(Optional.of(mockUser));

        assertThrows(NotAuthorizedException.class, () -> {
            var token = authService.authenticate(UserCredentialsDto.createNew("test", "password_invalid"));
            assertTrue(token.length() > 10);
        });
    }

    @Test
    void shouldNotAuthenticateUserWhenUserNotFoundInDatabase() {
        when(userRepository.getByName(anyString())).thenReturn(Optional.empty());

        assertThrows(NotAuthorizedException.class, () -> {
            var token = authService.authenticate(UserCredentialsDto.createNew("test", "password"));
            assertTrue(token.length() > 10);
        });
    }

}