package ru.itmo.is.controller;

import ru.itmo.is.entity.*;
import ru.itmo.is.service.TicketService;
import ru.itmo.is.service.VenueService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class SpecialOpsBean implements Serializable {
    @Inject
    private TicketService ticketService;

    @Inject
    private VenueService venueService;

    private Long selectedVenueId;
    private List<Venue> venues;
    private Ticket minNumberTicket;
    private Ticket maxDiscountTicket;
    private List<Ticket> minNumberTicketList;
    private List<Ticket> maxDiscountTicketList;

    private Integer sellTicketId;
    private Double sellPrice;
    private Person sellPerson;

    private String cancelPassportId;

    @PostConstruct
    public void init() {
        reload();
    }

    public void reload() {
        venues = venueService.getAllVenues();
        if (sellPerson == null) {
            sellPerson = new Person();
            sellPerson.setLocation(new Location());
        }
        if (minNumberTicketList == null) {
            minNumberTicketList = new java.util.ArrayList<>();
        }
        if (maxDiscountTicketList == null) {
            maxDiscountTicketList = new java.util.ArrayList<>();
        }
    }

    public void deleteByVenue() {
        try {
            ticketService.deleteByVenue(selectedVenueId);
            addMessage("Tickets deleted successfully");
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    public void findMinNumber() {
        minNumberTicket = ticketService.getMinNumberTicket();
        minNumberTicketList.clear();
        if (minNumberTicket != null) {
            minNumberTicketList.add(minNumberTicket);
        }
    }

    public void findMaxDiscount() {
        maxDiscountTicket = ticketService.getMaxDiscountTicket();
        maxDiscountTicketList.clear();
        if (maxDiscountTicket != null) {
            maxDiscountTicketList.add(maxDiscountTicket);
        }
    }

    public void sellTicket() {
        try {
            ticketService.sellTicket(sellTicketId, sellPrice, sellPerson);
            addMessage("Ticket sold successfully");
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    public void cancelBookings() {
        try {
            ticketService.cancelBookings(cancelPassportId);
            addMessage("Bookings cancelled successfully");
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    private void addMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public Long getSelectedVenueId() {
        return selectedVenueId;
    }

    public void setSelectedVenueId(Long selectedVenueId) {
        this.selectedVenueId = selectedVenueId;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public Ticket getMinNumberTicket() {
        return minNumberTicket;
    }

    public Ticket getMaxDiscountTicket() {
        return maxDiscountTicket;
    }

    public Integer getSellTicketId() {
        return sellTicketId;
    }

    public void setSellTicketId(Integer sellTicketId) {
        this.sellTicketId = sellTicketId;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Person getSellPerson() {
        return sellPerson;
    }

    public void setSellPerson(Person sellPerson) {
        this.sellPerson = sellPerson;
    }

    public String getCancelPassportId() {
        return cancelPassportId;
    }

    public void setCancelPassportId(String cancelPassportId) {
        this.cancelPassportId = cancelPassportId;
    }

    public Color[] getColors() {
        return Color.values();
    }

    public Country[] getCountries() {
        return Country.values();
    }

    public List<Ticket> getMinNumberTicketList() {
        return minNumberTicketList;
    }

    public List<Ticket> getMaxDiscountTicketList() {
        return maxDiscountTicketList;
    }
}
