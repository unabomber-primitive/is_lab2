package ru.itmo.is.dao;

import ru.itmo.is.entity.ImportHistory;
import ru.itmo.is.util.TransactionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class ImportHistoryDAO implements Serializable {
    @Inject
    private TransactionManager tm;

    public ImportHistory create(ImportHistory history) {
        return tm.executeInTransaction(em -> {
            em.persist(history);
            em.flush();
            return history;
        });
    }

    public ImportHistory update(ImportHistory history) {
        return tm.executeInTransaction(em -> em.merge(history));
    }

    public List<ImportHistory> findAll() {
        return tm.executeQuery(em ->
            em.createQuery("SELECT h FROM ImportHistory h ORDER BY h.timestamp DESC", ImportHistory.class)
                .getResultList()
        );
    }

    public List<ImportHistory> findByUsername(String username) {
        return tm.executeQuery(em ->
            em.createQuery("SELECT h FROM ImportHistory h WHERE h.username = :username ORDER BY h.timestamp DESC", ImportHistory.class)
                .setParameter("username", username)
                .getResultList()
        );
    }
}
