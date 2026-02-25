package ru.itmo.is.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String sql = readSqlFile();
            executeSql(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private String readSqlFile() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("db-functions.sql");
        if (is == null) {
            throw new RuntimeException("db-functions.sql not found");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private void executeSql(String sql) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TicketPU");
        EntityManager em = emf.createEntityManager();

        try {
            String[] statements = sql.split("\\$\\$ LANGUAGE plpgsql;");

            for (String statement : statements) {
                if (statement.trim().isEmpty()) {
                    continue;
                }
                String fullStatement = statement.trim() + "$$ LANGUAGE plpgsql;";

                em.getTransaction().begin();
                try {
                    em.createNativeQuery(fullStatement).executeUpdate();
                    em.getTransaction().commit();
                } catch (Exception e) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                }
            }
        } finally {
            em.close();
            emf.close();
        }
    }
}
