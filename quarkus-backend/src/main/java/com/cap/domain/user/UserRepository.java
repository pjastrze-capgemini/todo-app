package com.cap.domain.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext
    EntityManager em;

    public void create(User user) {
        em.persist(user);
    }

    public List<User> getAll() {
        return em.createQuery("SELECT u FROM User u ", User.class)
                .getResultList();
    }

    public List<User> searchByName(String name) {
        return em.createQuery("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(:name)", User.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    public Optional<User> getByName(String name) {
        return em.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
                .setParameter("name", name)
                .getResultList()
                .stream()
                .findFirst();
    }

}
