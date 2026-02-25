package ru.itmo.is.dao;

import ru.itmo.is.entity.Event;
import ru.itmo.is.util.TransactionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class EventDAO implements Serializable {
    @Inject
    private TransactionManager tm;

    public Event create(Event event) {
        validate(event);
        return tm.executeInTransaction(em -> {
            List<Event> existing = em.createQuery(
                    "SELECT e FROM Event e WHERE e.name = :name AND e.date = :date", Event.class)
                .setParameter("name", event.getName())
                .setParameter("date", event.getDate())
                .getResultList();
            if (!existing.isEmpty()) {
                throw new RuntimeException("Event with name '" + event.getName() + "' and date '" + event.getDate() + "' already exists");
            }
            em.persist(event);
            em.flush();
            return event;
        }, TransactionManager.SERIALIZABLE);
    }

    public Event findById(Integer id) {
        return tm.executeQuery(em -> em.find(Event.class, id));
    }

    public List<Event> findAll() {
        return tm.executeQuery(em ->
            em.createQuery("SELECT e FROM Event e", Event.class)
                .getResultList()
        );
    }

    public Event findByNameAndDate(String name, java.util.Date date) {
        return tm.executeQuery(em -> {
            List<Event> results = em.createQuery("SELECT e FROM Event e WHERE e.name = :name AND e.date = :date", Event.class)
                .setParameter("name", name)
                .setParameter("date", date)
                .getResultList();
            return results.isEmpty() ? null : results.get(0);
        });
    }

    public Event update(Event event) {
        validate(event);
        return tm.executeInTransaction(em -> {
            List<Event> existing = em.createQuery(
                    "SELECT e FROM Event e WHERE e.name = :name AND e.date = :date AND e.id <> :id", Event.class)
                .setParameter("name", event.getName())
                .setParameter("date", event.getDate())
                .setParameter("id", event.getId())
                .getResultList();
            if (!existing.isEmpty()) {
                throw new RuntimeException("Event with name '" + event.getName() + "' and date '" + event.getDate() + "' already exists");
            }
            return em.merge(event);
        }, TransactionManager.SERIALIZABLE);
    }

    public void delete(Integer id) {
        tm.executeInTransactionVoid(em -> {
            Event event = em.find(Event.class, id);
            if (event != null) {
                em.remove(event);
            }
        });
    }

    private void validate(Event event) {
        if (event.getName() == null || event.getName().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }
        if (event.getDate() == null) {
            throw new RuntimeException("Date cannot be null");
        }
        if (event.getDescription() == null || event.getDescription().isEmpty()) {
            throw new RuntimeException("Description cannot be empty");
        }
    }
}
