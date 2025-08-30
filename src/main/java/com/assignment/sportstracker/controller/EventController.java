package com.assignment.sportstracker.controller;

import com.assignment.sportstracker.dto.EventStatusRequest;
import com.assignment.sportstracker.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/status")
    public ResponseEntity<String> updateStatus(@RequestBody @Valid EventStatusRequest request) {
        eventService.updateEventStatus(request.getEventId(), request.getLive());
        return ResponseEntity.ok("Event status updated");
    }
}