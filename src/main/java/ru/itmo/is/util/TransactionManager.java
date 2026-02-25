package ru.itmo.is.util;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;

@ApplicationScoped
public class TransactionManager implements Serializable {
    public static final String READ_COMMITTED  = "READ COMMITTED";
    public static final String REPEATABLE_READ = "REPEATABLE READ";
    public static final String SERIALIZABLE    = "SERIALIZABLE";

    private EntityManagerFactory emf;

    public TransactionManager() {
        emf = Persistence.createEntityManagerFactory("TicketPU");
    }

    public <T> T executeInTransaction(TransactionCallback<T> callback) {
        return executeInTransaction(callback, READ_COMMITTED);
    }

    public <T> T executeInTransaction(TransactionCallback<T> callback, String isolationLevel) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNativeQuery("SET TRANSACTION ISOLATION LEVEL " + isolationLevel).executeUpdate();
            T result = callback.execute(em);
            em.getTransaction().commit();
            return result;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public void executeInTransactionVoid(TransactionCallbackVoid callback) {
        executeInTransactionVoid(callback, READ_COMMITTED);
    }

    public void executeInTransactionVoid(TransactionCallbackVoid callback, String isolationLevel) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNativeQuery("SET TRANSACTION ISOLATION LEVEL " + isolationLevel).executeUpdate();
            callback.execute(em);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }

    public <T> T executeQuery(QueryCallback<T> callback) {
        EntityManager em = emf.createEntityManager();
        try {
            return callback.execute(em);
        } finally {
            em.close();
        }
    }

    public interface TransactionCallback<T> {
        T execute(EntityManager em);
    }

    public interface TransactionCallbackVoid {
        void execute(EntityManager em);
    }

    public interface QueryCallback<T> {
        T execute(EntityManager em);
    }
}
