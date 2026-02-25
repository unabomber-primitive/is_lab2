package ru.itmo.is.service;

import ru.itmo.is.dao.TicketDAO;
import ru.itmo.is.entity.Person;
import ru.itmo.is.entity.Ticket;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class TicketService implements Serializable {
    @Inject
    private TicketDAO ticketDAO;

    public Ticket createTicket(Ticket ticket) {
        return ticketDAO.create(ticket);
    }

    public Ticket getTicket(int id) {
        return ticketDAO.findById(id);
    }

    public List<Ticket> getAllTickets(int page, int size) {
        return ticketDAO.findAll(page, size);
    }

    public long getTicketCount() {
        return ticketDAO.count();
    }

    public Ticket updateTicket(Ticket ticket) {
        return ticketDAO.update(ticket);
    }

    public void deleteTicket(int id) {
        ticketDAO.delete(id);
    }

    public void deleteByVenue(Long venueId) {
        if (venueId != null) {
            ticketDAO.deleteByVenue(venueId);
        }
    }

    public Ticket getMinNumberTicket() {
        return ticketDAO.findMinNumber();
    }

    public Ticket getMaxDiscountTicket() {
        return ticketDAO.findMaxDiscount();
    }

    public void sellTicket(int ticketId, Double price, Person person) {
        ticketDAO.sellTicket(
            ticketId,
            price,
            person.getPassportID(),
            person.getEyeColor() != null ? person.getEyeColor().name() : null,
            person.getHairColor().name(),
            person.getWeight(),
            person.getNationality().name(),
            person.getLocation().getX(),
            person.getLocation().getY(),
            person.getLocation().getName()
        );
    }

    public void cancelBookings(String passportId) {
        ticketDAO.cancelBookings(passportId);
    }

}
