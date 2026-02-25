package ru.itmo.is.service;

import ru.itmo.is.entity.User;
import ru.itmo.is.util.TransactionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class UserService implements Serializable {
    @Inject
    private TransactionManager tm;

    public User authenticate(String username, String password) {
        return tm.executeQuery(em -> {
            List<User> results = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();
            return results.isEmpty() ? null : results.get(0);
        });
    }
}
