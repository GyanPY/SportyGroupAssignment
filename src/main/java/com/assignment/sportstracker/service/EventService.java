package com.assignment.sportstracker.service;

import com.assignment.sportstracker.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventService {
    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final Map<String, Event> eventMap = new ConcurrentHashMap<>();

    public void updateEventStatus(String eventId, boolean live) {
        eventMap.put(eventId, new Event(eventId, live));
        log.info("Updated event {} to status {}", eventId, live);
    }

    public Map<String, Event> getAllEvents() {
        return eventMap;
    }
}
