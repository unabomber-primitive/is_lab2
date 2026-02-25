package ru.itmo.is.controller;

import ru.itmo.is.entity.Address;
import ru.itmo.is.entity.Venue;
import ru.itmo.is.entity.VenueType;
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
public class VenueBean implements Serializable {
    @Inject
    private VenueService venueService;

    private List<Venue> venues;
    private List<Venue> filteredVenues;
    private Venue selectedVenue;
    private Venue newVenue;

    @PostConstruct
    public void init() {
        loadVenues();
        newVenue = new Venue();
        newVenue.setAddress(new Address());
    }

    public void loadVenues() {
        venues = venueService.getAllVenues();
    }

    public void createVenue() {
        try {
            venueService.createVenue(newVenue);
            addMessage("Venue created successfully");
            loadVenues();
            newVenue = new Venue();
            newVenue.setAddress(new Address());
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    public void updateVenue() {
        try {
            venueService.updateVenue(selectedVenue);
            addMessage("Venue updated successfully");
            loadVenues();
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    public void deleteVenue(Long id) {
        try {
            venueService.deleteVenue(id);
            addMessage("Venue deleted successfully");
            loadVenues();
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    private void addMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public Venue getSelectedVenue() {
        return selectedVenue;
    }

    public void setSelectedVenue(Venue selectedVenue) {
        this.selectedVenue = selectedVenue;
    }

    public Venue getNewVenue() {
        return newVenue;
    }

    public void setNewVenue(Venue newVenue) {
        this.newVenue = newVenue;
    }

    public VenueType[] getVenueTypes() {
        return VenueType.values();
    }

    public List<Venue> getFilteredVenues() {
        return filteredVenues;
    }

    public void setFilteredVenues(List<Venue> filteredVenues) {
        this.filteredVenues = filteredVenues;
    }
}
