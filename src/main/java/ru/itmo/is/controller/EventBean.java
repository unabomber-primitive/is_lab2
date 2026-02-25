package ru.itmo.is.controller;

import ru.itmo.is.entity.Event;
import ru.itmo.is.service.EventService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class EventBean implements Serializable {
    @Inject
    private EventService eventService;

    private List<Event> events;
    private List<Event> filteredEvents;
    private Event selectedEvent;
    private Event newEvent;

    @PostConstruct
    public void init() {
        loadEvents();
        newEvent = new Event();
        newEvent.setDate(new Date());
    }

    public void loadEvents() {
        events = eventService.getAllEvents();
    }

    public void createEvent() {
        try {
            eventService.createEvent(newEvent);
            addMessage("Event created successfully");
            loadEvents();
            newEvent = new Event();
            newEvent.setDate(new Date());
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    public void updateEvent() {
        try {
            eventService.updateEvent(selectedEvent);
            addMessage("Event updated successfully");
            loadEvents();
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    public void deleteEvent(Integer id) {
        try {
            eventService.deleteEvent(id);
            addMessage("Event deleted successfully");
            loadEvents();
        } catch (Exception e) {
            addMessage("Error: " + e.getMessage());
        }
    }

    private void addMessage(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
    }

    public List<Event> getEvents() {
        return events;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public Event getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(Event newEvent) {
        this.newEvent = newEvent;
    }

    public List<Event> getFilteredEvents() {
        return filteredEvents;
    }

    public void setFilteredEvents(List<Event> filteredEvents) {
        this.filteredEvents = filteredEvents;
    }
}
