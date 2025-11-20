package com.cap.domain.user;

import com.cap.api.dtos.RegisterUserDto;
import com.cap.api.dtos.UserCredentialsDto;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAuthorizedException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Set;

@ApplicationScoped
public class AuthService {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @Inject
    UserRepository repository;

    public String authenticate(UserCredentialsDto credentials) {
        var user = repository.getByName(credentials.login);
        if (user.isEmpty()) {
            throw new NotAuthorizedException("Invalid login or password");
        }

        var dbUser = user.get();
        if (!BCrypt.checkpw(credentials.password, dbUser.password)) {
            throw new NotAuthorizedException("Invalid login or password");
        }

        return Jwt.issuer("https://example.com/issuer")
                .upn("johndoe@gmail.com")
                .groups(Set.of("User"))
                .expiresIn(300L)
//                .claim(Claims.birthdate.name(), "2001-07-13")
                .sign();

//        System.out.println(issuer);
//        return Jwt.issuer(issuer)
//                .upn(dbUser.name)
//                .expiresIn(300L)
//                .groups(Set.of("User"))
//                .sign();
    }

    @Transactional
    public void registerUser(RegisterUserDto dto) {
        if(!dto.password.equals(dto.confirmPassword)) {
            throw new NotAuthorizedException("Passwords do not match.");
        }

        var dbUser = repository.getByName(dto.login);
        if (dbUser.isPresent()) {
            throw new NotAuthorizedException("Login is taken.");
        }

        var user = new User();
        user.name = dto.login;
        user.password = BCrypt.hashpw(dto.password, BCrypt.gensalt());
        repository.create(user);
    }

}
