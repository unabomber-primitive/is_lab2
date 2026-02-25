package ru.itmo.is.dao;

import ru.itmo.is.entity.Venue;
import ru.itmo.is.util.TransactionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class VenueDAO implements Serializable {
    @Inject
    private TransactionManager tm;

    public Venue create(Venue venue) {
        validate(venue);
        return tm.executeInTransaction(em -> {
            List<Venue> existing = em.createQuery(
                    "SELECT v FROM Venue v WHERE v.name = :name", Venue.class)
                .setParameter("name", venue.getName())
                .getResultList();
            if (!existing.isEmpty()) {
                throw new RuntimeException("Venue with name '" + venue.getName() + "' already exists");
            }
            em.persist(venue);
            em.flush();
            return venue;
        }, TransactionManager.SERIALIZABLE);
    }

    public Venue findById(Long id) {
        return tm.executeQuery(em -> em.find(Venue.class, id));
    }

    public List<Venue> findAll() {
        return tm.executeQuery(em ->
            em.createQuery("SELECT v FROM Venue v", Venue.class)
                .getResultList()
        );
    }

    public Venue findByName(String name) {
        return tm.executeQuery(em -> {
            List<Venue> results = em.createQuery("SELECT v FROM Venue v WHERE v.name = :name", Venue.class)
                .setParameter("name", name)
                .getResultList();
            return results.isEmpty() ? null : results.get(0);
        });
    }

    public Venue update(Venue venue) {
        validate(venue);
        return tm.executeInTransaction(em -> {
            List<Venue> existing = em.createQuery(
                    "SELECT v FROM Venue v WHERE v.name = :name AND v.id <> :id", Venue.class)
                .setParameter("name", venue.getName())
                .setParameter("id", venue.getId())
                .getResultList();
            if (!existing.isEmpty()) {
                throw new RuntimeException("Venue with name '" + venue.getName() + "' already exists");
            }
            return em.merge(venue);
        }, TransactionManager.SERIALIZABLE);
    }

    public void delete(Long id) {
        tm.executeInTransactionVoid(em -> {
            Venue venue = em.find(Venue.class, id);
            if (venue != null) {
                em.remove(venue);
            }
        });
    }

    private void validate(Venue venue) {
        if (venue.getName() == null || venue.getName().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }
        if (venue.getCapacity() <= 0) {
            throw new RuntimeException("Capacity must be greater than 0");
        }
        if (venue.getType() == null) {
            throw new RuntimeException("Type cannot be null");
        }
        if (venue.getAddress() != null && venue.getAddress().getZipCode() != null) {
            if (venue.getAddress().getZipCode().length() < 3) {
                throw new RuntimeException("Zip code must be at least 3 characters");
            }
        }
    }
}
