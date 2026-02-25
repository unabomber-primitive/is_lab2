package ru.itmo.is.service;

import ru.itmo.is.dao.VenueDAO;
import ru.itmo.is.entity.Venue;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class VenueService implements Serializable {
    @Inject
    private VenueDAO venueDAO;

    public Venue createVenue(Venue venue) {
        return venueDAO.create(venue);
    }

    public Venue getVenue(Long id) {
        return venueDAO.findById(id);
    }

    public List<Venue> getAllVenues() {
        return venueDAO.findAll();
    }

    public Venue updateVenue(Venue venue) {
        return venueDAO.update(venue);
    }

    public void deleteVenue(Long id) {
        venueDAO.delete(id);
    }

}
