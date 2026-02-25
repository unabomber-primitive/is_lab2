package ru.itmo.is.dao;

import ru.itmo.is.entity.Person;
import ru.itmo.is.entity.Ticket;
import ru.itmo.is.entity.Venue;
import ru.itmo.is.util.TransactionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class TicketDAO implements Serializable {
    @Inject
    private TransactionManager tm;

    public Ticket create(Ticket ticket) {
        validate(ticket);
        return tm.executeInTransaction(em -> {
            List<Ticket> existing = em.createQuery(
                    "SELECT t FROM Ticket t WHERE t.number = :number", Ticket.class)
                .setParameter("number", ticket.getNumber())
                .getResultList();
            if (!existing.isEmpty()) {
                throw new RuntimeException("Ticket with number " + ticket.getNumber() + " already exists");
            }
            em.persist(ticket);
            em.flush();
            return ticket;
        }, TransactionManager.SERIALIZABLE);
    }

    public Ticket findById(int id) {
        return tm.executeQuery(em -> em.find(Ticket.class, id));
    }

    public List<Ticket> findAll(int page, int size) {
        return tm.executeQuery(em ->
            em.createQuery("SELECT t FROM Ticket t", Ticket.class)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList()
        );
    }

    public long count() {
        return tm.executeQuery(em ->
            em.createQuery("SELECT COUNT(t) FROM Ticket t", Long.class)
                .getSingleResult()
        );
    }

    public Ticket findByNumber(long number) {
        return tm.executeQuery(em -> {
            List<Ticket> results = em.createQuery("SELECT t FROM Ticket t WHERE t.number = :number", Ticket.class)
                .setParameter("number", number)
                .getResultList();
            return results.isEmpty() ? null : results.get(0);
        });
    }

    public List<Ticket> findByFilter(String column, String value, int page, int size) {
        return tm.executeQuery(em ->
            em.createQuery("SELECT t FROM Ticket t WHERE t." + column + " = :value", Ticket.class)
                .setParameter("value", value)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList()
        );
    }

    public Ticket update(Ticket ticket) {
        validate(ticket);
        return tm.executeInTransaction(em -> {
            List<Ticket> existing = em.createQuery(
                    "SELECT t FROM Ticket t WHERE t.number = :number AND t.id <> :id", Ticket.class)
                .setParameter("number", ticket.getNumber())
                .setParameter("id", ticket.getId())
                .getResultList();
            if (!existing.isEmpty()) {
                throw new RuntimeException("Ticket with number " + ticket.getNumber() + " already exists");
            }
            return em.merge(ticket);
        }, TransactionManager.SERIALIZABLE);
    }

    public void delete(int id) {
        tm.executeInTransactionVoid(em -> {
            Ticket ticket = em.find(Ticket.class, id);
            if (ticket != null) {
                em.remove(ticket);
            }
        });
    }

    public int deleteByVenue(Long venueId) {
        return tm.executeInTransaction(em ->
            (Integer) em.createNativeQuery("SELECT delete_tickets_by_venue(?)")
                .setParameter(1, venueId)
                .getSingleResult()
        );
    }

    public Ticket findMinNumber() {
        return tm.executeQuery(em -> {
            List<Object[]> results = em.createNativeQuery(
                "SELECT * FROM find_ticket_with_min_number()"
            ).getResultList();

            if (results.isEmpty()) {
                return null;
            }

            Object[] row = results.get(0);
            int id = (Integer) row[0];
            return em.find(Ticket.class, id);
        });
    }

    public Ticket findMaxDiscount() {
        return tm.executeQuery(em -> {
            List<Object[]> results = em.createNativeQuery(
                "SELECT * FROM find_ticket_with_max_discount()"
            ).getResultList();

            if (results.isEmpty()) {
                return null;
            }

            Object[] row = results.get(0);
            int id = (Integer) row[0];
            return em.find(Ticket.class, id);
        });
    }

    public void sellTicket(int ticketId, double price, String passportId, String eyeColor,
                          String hairColor, double weight, String nationality,
                          Float locationX, Long locationY, String locationName) {
        tm.executeInTransactionVoid(em ->
            em.createNativeQuery(
                "SELECT sell_ticket_to_person(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            )
            .setParameter(1, ticketId)
            .setParameter(2, price)
            .setParameter(3, passportId)
            .setParameter(4, eyeColor)
            .setParameter(5, hairColor)
            .setParameter(6, weight)
            .setParameter(7, nationality)
            .setParameter(8, locationX)
            .setParameter(9, locationY)
            .setParameter(10, locationName)
            .getSingleResult()
        );
    }

    public int cancelBookings(String passportId) {
        return tm.executeInTransaction(em ->
            (Integer) em.createNativeQuery("SELECT cancel_bookings_by_passport(?)")
                .setParameter(1, passportId)
                .getSingleResult()
        );
    }

    private void validate(Ticket ticket) {
        if (ticket.getName() == null || ticket.getName().isEmpty()) {
            throw new RuntimeException("Name cannot be empty");
        }
        if (ticket.getCoordinates() == null) {
            throw new RuntimeException("Coordinates cannot be null");
        }
        if (ticket.getCoordinates().getY() == null || ticket.getCoordinates().getY() <= -196) {
            throw new RuntimeException("Y must be greater than -196");
        }
        if (ticket.getEvent() == null) {
            throw new RuntimeException("Event cannot be null");
        }
        if (ticket.getPrice() == null || ticket.getPrice() <= 0) {
            throw new RuntimeException("Price must be greater than 0");
        }
        if (ticket.getType() == null) {
            throw new RuntimeException("Type cannot be null");
        }
        if (ticket.getDiscount() == null || ticket.getDiscount() <= 0 || ticket.getDiscount() > 100) {
            throw new RuntimeException("Discount must be between 1 and 100");
        }
        if (ticket.getNumber() <= 0) {
            throw new RuntimeException("Number must be greater than 0");
        }
        if (ticket.getVenue() == null) {
            throw new RuntimeException("Venue cannot be null");
        }
        if (ticket.getPerson() != null) {
            Person p = ticket.getPerson();
            if (p.getPassportID() != null && !p.getPassportID().isEmpty()) {
                if (p.getHairColor() == null) {
                    throw new RuntimeException("Hair color cannot be null");
                }
                if (p.getLocation() == null) {
                    throw new RuntimeException("Person location cannot be null");
                }
                if (p.getLocation().getX() == null) {
                    throw new RuntimeException("Person location X cannot be null");
                }
                if (p.getLocation().getY() == null) {
                    throw new RuntimeException("Person location Y cannot be null");
                }
                if (p.getLocation().getName() != null && p.getLocation().getName().length() > 709) {
                    throw new RuntimeException("Location name must not exceed 709 characters");
                }
                if (p.getWeight() <= 0) {
                    throw new RuntimeException("Weight must be greater than 0");
                }
                if (p.getPassportID().length() < 10 || p.getPassportID().length() > 35) {
                    throw new RuntimeException("Passport ID must be between 10 and 35 characters");
                }
                if (p.getNationality() == null) {
                    throw new RuntimeException("Nationality cannot be null");
                }
            } else {
                ticket.setPerson(null);
            }
        }
    }
}
