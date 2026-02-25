package ru.itmo.is.service;

import ru.itmo.is.dao.EventDAO;
import ru.itmo.is.entity.Event;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
public class EventService implements Serializable {
    @Inject
    private EventDAO eventDAO;

    public Event createEvent(Event event) {
        return eventDAO.create(event);
    }

    public Event getEvent(Integer id) {
        return eventDAO.findById(id);
    }

    public List<Event> getAllEvents() {
        return eventDAO.findAll();
    }

    public Event updateEvent(Event event) {
        return eventDAO.update(event);
    }

    public void deleteEvent(Integer id) {
        eventDAO.delete(id);
    }

}
