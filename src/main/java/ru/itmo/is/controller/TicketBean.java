package ru.itmo.is.controller;

import ru.itmo.is.entity.*;
import ru.itmo.is.service.EventService;
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
public class TicketBean implements Serializable {
    @Inject
    private TicketService ticketService;

    @Inject
    private EventService eventService;

    @Inject
    private VenueService venueService;

    private List<Ticket> tickets;
    private List<Ticket> filteredTickets;
    private Ticket selectedTicket;
    private Ticket newTicket;
    private List<Event> events;
    private List<Venue> venues;

    private Integer selectedEventId;
    private Long selectedVenueId;
    private Integer editEventId;
    private Long editVenueId;

    private String newPersonPassportId;
    private Color newPersonEyeColor;
    private Color newPersonHairColor;
    private Double newPersonWeight;
    private Country newPersonNationality;
    private Float newPersonLocationX;
    private Long newPersonLocationY;
    private String newPersonLocationName;

    private String editPersonPassportId;
    private Color editPersonEyeColor;
    private Color editPersonHairColor;
    private Double editPersonWeight;
    private Country editPersonNationality;
    private Float editPersonLocationX;
    private Long editPersonLocationY;
    private String editPersonLocationName;

    private int page = 0;
    private int pageSize = 10;
    private long totalRecords;

    @PostConstruct
    public void init() {
        loadTickets();
        events = eventService.getAllEvents();
        venues = venueService.getAllVenues();
        newTicket = new Ticket();
        newTicket.setCoordinates(new Coordinates());
    }

    public void loadTickets() {
        tickets = ticketService.getAllTickets(page, pageSize);
        totalRecords = ticketService.getTicketCount();
    }

    public void createTicket() {
        try {
            if (selectedEventId != null) {
                newTicket.setEvent(eventService.getEvent(selectedEventId));
            }
            if (selectedVenueId != null) {
                newTicket.setVenue(venueService.getVenue(selectedVenueId));
            }
            if (newPersonPassportId != null && !newPersonPassportId.trim().isEmpty()) {
                Person person = new Person();
                person.setPassportID(newPersonPassportId);
                person.setEyeColor(newPersonEyeColor);
                person.setHairColor(newPersonHairColor);
                person.setWeight(newPersonWeight != null ? newPersonWeight : 0);
                person.setNationality(newPersonNationality);
                Location location = new Location();
                location.setX(newPersonLocationX);
                location.setY(newPersonLocationY);
                location.setName(newPersonLocationName);
                person.setLocation(location);
                newTicket.setPerson(person);
            }
            ticketService.createTicket(newTicket);
            addMessage("Ticket created successfully");
            loadTickets();
            resetNewTicketForm();
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    private void resetNewTicketForm() {
        newTicket = new Ticket();
        newTicket.setCoordinates(new Coordinates());
        selectedEventId = null;
        selectedVenueId = null;
        newPersonPassportId = null;
        newPersonEyeColor = null;
        newPersonHairColor = null;
        newPersonWeight = null;
        newPersonNationality = null;
        newPersonLocationX = null;
        newPersonLocationY = null;
        newPersonLocationName = null;
    }

    public void updateTicket() {
        try {
            if (editEventId != null) {
                selectedTicket.setEvent(eventService.getEvent(editEventId));
            }
            if (editVenueId != null) {
                selectedTicket.setVenue(venueService.getVenue(editVenueId));
            }
            if (editPersonPassportId != null && !editPersonPassportId.trim().isEmpty()) {
                Person person = new Person();
                person.setPassportID(editPersonPassportId);
                person.setEyeColor(editPersonEyeColor);
                person.setHairColor(editPersonHairColor);
                person.setWeight(editPersonWeight != null ? editPersonWeight : 0);
                person.setNationality(editPersonNationality);
                Location location = new Location();
                location.setX(editPersonLocationX);
                location.setY(editPersonLocationY);
                location.setName(editPersonLocationName);
                person.setLocation(location);
                selectedTicket.setPerson(person);
            } else {
                selectedTicket.setPerson(null);
            }
            ticketService.updateTicket(selectedTicket);
            addMessage("Ticket updated successfully");
            loadTickets();
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    public void deleteTicket(int id) {
        try {
            ticketService.deleteTicket(id);
            addMessage("Ticket deleted successfully");
            loadTickets();
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    public void nextPage() {
        if ((page + 1) * pageSize < totalRecords) {
            page++;
            loadTickets();
        }
    }

    public void previousPage() {
        if (page > 0) {
            page--;
            loadTickets();
        }
    }

    private void addMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Ticket getSelectedTicket() {
        return selectedTicket;
    }

    public void setSelectedTicket(Ticket selectedTicket) {
        this.selectedTicket = selectedTicket;
        if (selectedTicket != null) {
            editEventId = selectedTicket.getEvent() != null ? selectedTicket.getEvent().getId() : null;
            editVenueId = selectedTicket.getVenue() != null ? selectedTicket.getVenue().getId() : null;
            if (selectedTicket.getPerson() != null) {
                Person p = selectedTicket.getPerson();
                editPersonPassportId = p.getPassportID();
                editPersonEyeColor = p.getEyeColor();
                editPersonHairColor = p.getHairColor();
                editPersonWeight = p.getWeight();
                editPersonNationality = p.getNationality();
                if (p.getLocation() != null) {
                    editPersonLocationX = p.getLocation().getX();
                    editPersonLocationY = p.getLocation().getY();
                    editPersonLocationName = p.getLocation().getName();
                } else {
                    editPersonLocationX = null;
                    editPersonLocationY = null;
                    editPersonLocationName = null;
                }
            } else {
                editPersonPassportId = null;
                editPersonEyeColor = null;
                editPersonHairColor = null;
                editPersonWeight = null;
                editPersonNationality = null;
                editPersonLocationX = null;
                editPersonLocationY = null;
                editPersonLocationName = null;
            }
        }
    }

    public Ticket getNewTicket() {
        return newTicket;
    }

    public void setNewTicket(Ticket newTicket) {
        this.newTicket = newTicket;
    }

    public List<Event> getEvents() {
        return events;
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalRecords() {
        return totalRecords;
    }

    public TicketType[] getTicketTypes() {
        return TicketType.values();
    }

    public Color[] getColors() {
        return Color.values();
    }

    public Country[] getCountries() {
        return Country.values();
    }

    public Integer getSelectedEventId() {
        return selectedEventId;
    }

    public void setSelectedEventId(Integer selectedEventId) {
        this.selectedEventId = selectedEventId;
    }

    public Long getSelectedVenueId() {
        return selectedVenueId;
    }

    public void setSelectedVenueId(Long selectedVenueId) {
        this.selectedVenueId = selectedVenueId;
    }

    public List<Ticket> getFilteredTickets() {
        return filteredTickets;
    }

    public void setFilteredTickets(List<Ticket> filteredTickets) {
        this.filteredTickets = filteredTickets;
    }

    public String getNewPersonPassportId() {
        return newPersonPassportId;
    }

    public void setNewPersonPassportId(String newPersonPassportId) {
        this.newPersonPassportId = newPersonPassportId;
    }

    public Color getNewPersonEyeColor() {
        return newPersonEyeColor;
    }

    public void setNewPersonEyeColor(Color newPersonEyeColor) {
        this.newPersonEyeColor = newPersonEyeColor;
    }

    public Color getNewPersonHairColor() {
        return newPersonHairColor;
    }

    public void setNewPersonHairColor(Color newPersonHairColor) {
        this.newPersonHairColor = newPersonHairColor;
    }

    public Double getNewPersonWeight() {
        return newPersonWeight;
    }

    public void setNewPersonWeight(Double newPersonWeight) {
        this.newPersonWeight = newPersonWeight;
    }

    public Country getNewPersonNationality() {
        return newPersonNationality;
    }

    public void setNewPersonNationality(Country newPersonNationality) {
        this.newPersonNationality = newPersonNationality;
    }

    public Float getNewPersonLocationX() {
        return newPersonLocationX;
    }

    public void setNewPersonLocationX(Float newPersonLocationX) {
        this.newPersonLocationX = newPersonLocationX;
    }

    public Long getNewPersonLocationY() {
        return newPersonLocationY;
    }

    public void setNewPersonLocationY(Long newPersonLocationY) {
        this.newPersonLocationY = newPersonLocationY;
    }

    public String getNewPersonLocationName() {
        return newPersonLocationName;
    }

    public void setNewPersonLocationName(String newPersonLocationName) {
        this.newPersonLocationName = newPersonLocationName;
    }

    public String getEditPersonPassportId() {
        return editPersonPassportId;
    }

    public void setEditPersonPassportId(String editPersonPassportId) {
        this.editPersonPassportId = editPersonPassportId;
    }

    public Color getEditPersonEyeColor() {
        return editPersonEyeColor;
    }

    public void setEditPersonEyeColor(Color editPersonEyeColor) {
        this.editPersonEyeColor = editPersonEyeColor;
    }

    public Color getEditPersonHairColor() {
        return editPersonHairColor;
    }

    public void setEditPersonHairColor(Color editPersonHairColor) {
        this.editPersonHairColor = editPersonHairColor;
    }

    public Double getEditPersonWeight() {
        return editPersonWeight;
    }

    public void setEditPersonWeight(Double editPersonWeight) {
        this.editPersonWeight = editPersonWeight;
    }

    public Country getEditPersonNationality() {
        return editPersonNationality;
    }

    public void setEditPersonNationality(Country editPersonNationality) {
        this.editPersonNationality = editPersonNationality;
    }

    public Float getEditPersonLocationX() {
        return editPersonLocationX;
    }

    public void setEditPersonLocationX(Float editPersonLocationX) {
        this.editPersonLocationX = editPersonLocationX;
    }

    public Long getEditPersonLocationY() {
        return editPersonLocationY;
    }

    public void setEditPersonLocationY(Long editPersonLocationY) {
        this.editPersonLocationY = editPersonLocationY;
    }

    public String getEditPersonLocationName() {
        return editPersonLocationName;
    }

    public void setEditPersonLocationName(String editPersonLocationName) {
        this.editPersonLocationName = editPersonLocationName;
    }

    public Integer getEditEventId() {
        return editEventId;
    }

    public void setEditEventId(Integer editEventId) {
        this.editEventId = editEventId;
    }

    public Long getEditVenueId() {
        return editVenueId;
    }

    public void setEditVenueId(Long editVenueId) {
        this.editVenueId = editVenueId;
    }
}
