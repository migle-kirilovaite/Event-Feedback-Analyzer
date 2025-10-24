package com.example.eventsync.controller;

import com.example.eventsync.dto.CreateEventRequest;
import com.example.eventsync.dto.EventResponse;
import com.example.eventsync.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createdEvent(@Valid @RequestBody CreateEventRequest request)  {
        EventResponse created = eventService.createEvent(request);
        URI location = URI.create(String.format("/events/%s", created.getId()));

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> listEvents() {
        List<EventResponse> events = eventService.listEvents();

        return ResponseEntity.ok(events);
    }

}
