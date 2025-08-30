package com.assignment.sportstracker.scheduler;

import com.assignment.sportstracker.model.Event;
import com.assignment.sportstracker.service.EventService;
import com.assignment.sportstracker.service.ExternalApiService;
import com.assignment.sportstracker.service.MessagePublisherService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventScheduler {
    private final EventService eventService;
    private final ExternalApiService externalApiService;
    private final MessagePublisherService publisherService;

    public EventScheduler(EventService eventService, ExternalApiService externalApiService, MessagePublisherService publisherService) {
        this.eventService = eventService;
        this.externalApiService = externalApiService;
        this.publisherService = publisherService;
    }

    @Scheduled(fixedRate = 10000) // every 10 seconds
    public void pollLiveEvents() {
        eventService.getAllEvents().values().stream()
            .filter(Event::isLive)
            .forEach(event -> {
                var response = externalApiService.fetchEventData(event.getEventId());
                publisherService.publish("sports-events", response);
            }
        );
    }
}