package com.cap.domain.user;

import com.cap.api.dtos.RegisterUserDto;
import com.cap.api.dtos.UserCredentialsDto;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAuthorizedException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class AuthService {

    @Inject
    SecurityIdentity identity;

    public Optional<User> getAuthUser() {
        if (identity.isAnonymous()) {
            return Optional.empty();
        }

        return repository.getByName(identity.getPrincipal().getName());
    }

    public User getAuthUserOrThrow() {
        return this.getAuthUser().orElseThrow(() -> new NotAuthorizedException("Unauthorized."));
    }

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @Inject
    UserRepository repository;

    public String authenticate(UserCredentialsDto credentials) {
        var user = repository.getByName(credentials.login);
        if (user.isEmpty()) {
            throw new NotAuthorizedException("Invalid login or password.");
        }

        var dbUser = user.get();
        if (!PasswordUtil.verifyPassword(credentials.password, dbUser.password)) {
            throw new NotAuthorizedException("Invalid login or password.");
        }

        return Jwt.issuer(issuer)
                .upn(dbUser.name)
                .expiresIn(300L)
                .groups(Set.of("User"))
                .sign();
    }

    @Transactional
    public User registerUser(RegisterUserDto dto) {
        if (!dto.password.equals(dto.confirmPassword)) {
            throw new NotAuthorizedException("Passwords do not match.");
        }

        var dbUser = repository.getByName(dto.login);
        if (dbUser.isPresent()) {
            throw new NotAuthorizedException("Login is taken.");
        }

        var user = new User();
        user.name = dto.login;
        user.password = PasswordUtil.hashPassword(dto.password);
        repository.create(user);
        return user;
    }

}
