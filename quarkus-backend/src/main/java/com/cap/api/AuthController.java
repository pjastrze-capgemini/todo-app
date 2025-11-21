package com.cap.api;

import com.cap.api.dtos.RegisterUserDto;
import com.cap.api.dtos.UserCredentialsDto;
import com.cap.api.dtos.UserDto;
import com.cap.domain.user.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/auth")
@PermitAll
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    AuthService authService;

    @POST
    @Path("/login")
    public Response login(UserCredentialsDto credentials) {
        var token = authService.authenticate(credentials);
        return Response.ok(Map.of("token", token)).build();
    }

    @POST
    @Path("/register")
    public Response register(RegisterUserDto dto) {
        authService.registerUser(dto);
        return Response
                .ok(Map.of("message", "User created"))
                .status(Response.Status.CREATED)
                .build();
    }

    @GET
    @Path("/me")
    public UserDto userInfo() {
        var user = authService.getAuthUserOrThrow();
        return UserDto.from(user);
    }

}