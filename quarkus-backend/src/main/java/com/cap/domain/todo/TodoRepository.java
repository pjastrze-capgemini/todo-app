package com.cap.domain.todo;

import com.cap.domain.user.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotAllowedException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class TodoRepository {

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Todo create(User user, Todo todo) {
        em.merge(user);

        todo.owner = user;
        todo.createAt = Instant.now();
        em.persist(todo);
        return todo;
    }

    @Transactional
    public Todo update(User user, Todo todo) {
        if (!todo.owner.id.equals((user.id))) {
            throw new NotAllowedException("Forbidden");
        }
        em.persist(todo);
        return todo;
    }

    @Transactional
    public void delete(User user, Todo todo) {
        if (!todo.owner.id.equals((user.id))) {
            throw new NotAllowedException("Forbidden");
        }
        em.remove((todo));
    }

    public List<Todo> getUsersTodos(User user) {
        return em.createQuery("SELECT t FROM Todo t ", Todo.class)
                .getResultList();
    }

    public List<Todo> searchByTitle(User user, String title) {
        String query = """
                SELECT t FROM Todo t
                WHERE LOWER(t.title) LIKE LOWER(:title)
                AND t.owner.id = :owner_id
                """;

        return em.createQuery(query, Todo.class)
                .setParameter("title", "%" + title + "%")
                .setParameter("owner_id", user.id)
                .getResultList();
    }

    public Optional<Todo> findById(User user, Long TodoId) {
        String query = """
                SELECT t FROM Todo t
                WHERE t.id = :id
                AND t.owner.id = :owner_id
                """;

        return em.createQuery(query, Todo.class)
                .setParameter("id", TodoId)
                .setParameter("owner_id", user.id)
                .getResultList()
                .stream()
                .findFirst();
    }

}
