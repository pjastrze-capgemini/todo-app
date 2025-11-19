package com.cap.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository {

    @PersistenceContext
    EntityManager em;

    public List<User> getAll() {
        System.out.println("Get all users:");
        return em.createQuery("SELECT u FROM User u ", User.class)
                .getResultList();
    }

    public List<User> findByName(String name) {
        System.out.println(name);
        return em.createQuery("SELECT u FROM User u WHERE LOWER(u.name) LIKE LOWER(:name)", User.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

}
