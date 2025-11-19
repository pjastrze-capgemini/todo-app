package com.cap.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import com.cap.user.UserRepository;
import jakarta.inject.Inject;
import com.cap.user.*;
import java.util.List;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;


@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Inject
    UserRepository repository;

    @GET
    // @Produces(MediaType.TEXT_PLAIN)
    public List<User> hello() {
        // var users = repository.findByName("a");
        var users =  repository.getAll();
        System.out.println(users);
        return users;
    }
}