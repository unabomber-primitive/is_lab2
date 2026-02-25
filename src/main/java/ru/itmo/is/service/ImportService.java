package ru.itmo.is.service;

import ru.itmo.is.dao.EventDAO;
import ru.itmo.is.dao.TicketDAO;
import ru.itmo.is.dao.VenueDAO;
import ru.itmo.is.dto.TicketImportDTO;
import ru.itmo.is.entity.*;
import ru.itmo.is.util.TransactionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ImportService implements Serializable {
    @Inject
    private TransactionManager tm;

    @Inject
    private EventDAO eventDAO;

    @Inject
    private VenueDAO venueDAO;

    @Inject
    private TicketService ticketService;

    public int importTickets(InputStream inputStream) {
        List<Ticket> tickets = parseJson(inputStream);

        return tm.executeInTransaction(em -> {
            int count = 0;
            for (Ticket ticket : tickets) {
                ticketService.createTicket(ticket);
                count++;
            }
            return count;
        });
    }

    private List<Ticket> parseJson(InputStream inputStream) {
        List<Ticket> tickets = new ArrayList<>();

        try (JsonReader reader = Json.createReader(inputStream)) {
            JsonArray jsonArray = reader.readArray();

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject json = jsonArray.getJsonObject(i);
                tickets.add(jsonToTicket(json));
            }
        }

        return tickets;
    }

    private Ticket jsonToTicket(JsonObject json) {
        Ticket ticket = new Ticket();
        ticket.setName(json.getString("name"));
        ticket.setNumber(json.getJsonNumber("number").longValue());
        ticket.setPrice(json.getJsonNumber("price").doubleValue());
        ticket.setDiscount(json.getJsonNumber("discount").longValue());
        ticket.setType(TicketType.valueOf(json.getString("type")));

        JsonObject coordJson = json.getJsonObject("coordinates");
        Coordinates coords = new Coordinates();
        coords.setX((float) coordJson.getJsonNumber("x").doubleValue());
        coords.setY(coordJson.getJsonNumber("y").doubleValue());
        ticket.setCoordinates(coords);

        Event event = eventDAO.findById(json.getInt("eventId"));
        if (event == null) {
            throw new RuntimeException("Event not found: " + json.getInt("eventId"));
        }
        ticket.setEvent(event);

        Venue venue = venueDAO.findById(json.getJsonNumber("venueId").longValue());
        if (venue == null) {
            throw new RuntimeException("Venue not found: " + json.getJsonNumber("venueId").longValue());
        }
        ticket.setVenue(venue);

        if (!json.isNull("person")) {
            JsonObject personJson = json.getJsonObject("person");
            Person person = new Person();
            person.setPassportID(personJson.getString("passportID"));
            person.setHairColor(Color.valueOf(personJson.getString("hairColor")));
            if (!personJson.isNull("eyeColor")) {
                person.setEyeColor(Color.valueOf(personJson.getString("eyeColor")));
            }
            person.setWeight(personJson.getJsonNumber("weight").doubleValue());
            person.setNationality(Country.valueOf(personJson.getString("nationality")));

            JsonObject locJson = personJson.getJsonObject("location");
            Location location = new Location();
            location.setX((float) locJson.getJsonNumber("x").doubleValue());
            location.setY(locJson.getJsonNumber("y").longValue());
            if (!locJson.isNull("name")) {
                location.setName(locJson.getString("name"));
            }
            person.setLocation(location);

            ticket.setPerson(person);
        }

        return ticket;
    }
}
