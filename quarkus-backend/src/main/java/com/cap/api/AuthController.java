package com.cap.api;

import com.cap.api.dtos.RegisterUserDto;
import com.cap.api.dtos.UserCredentialsDto;
import com.cap.domain.user.AuthService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/auth")
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

}