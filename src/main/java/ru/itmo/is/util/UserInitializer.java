package ru.itmo.is.util;

import ru.itmo.is.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class UserInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== UserInitializer starting ===");
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("TicketPU");
            em = emf.createEntityManager();

            em.getTransaction().begin();
            System.out.println("Transaction started");

            long count = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
            System.out.println("Current user count: " + count);

            if (count == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin");
                admin.setRole("ADMIN");
                em.persist(admin);
                System.out.println("Admin user persisted");

                User user = new User();
                user.setUsername("user");
                user.setPassword("user");
                user.setRole("USER");
                em.persist(user);
                System.out.println("Regular user persisted");

                em.getTransaction().commit();
                System.out.println("=== Default users created: admin/admin, user/user ===");
            } else {
                em.getTransaction().commit();
                System.out.println("=== Users already exist, skipping creation ===");
            }
        } catch (Exception e) {
            System.err.println("=== UserInitializer ERROR ===");
            e.printStackTrace();
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) em.close();
            if (emf != null) emf.close();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
