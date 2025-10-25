package com.example.eventsync.controller;

import com.example.eventsync.dto.CreateEventRequest;
import com.example.eventsync.dto.EventResponse;
import com.example.eventsync.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(
        path = "/events",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
@Tag(name = "Events", description = "Event management endpoints")
public class EventController {
    private final EventService eventService;

    @Operation(summary = "Create new event")
    @PostMapping
    public ResponseEntity<EventResponse> createdEvent(@Valid @RequestBody CreateEventRequest request)  {
        EventResponse created = eventService.createEvent(request);
        URI location = URI.create(String.format("/events/%s", created.getId()));

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Get all events")
    @GetMapping
    public ResponseEntity<List<EventResponse>> listEvents() {
        List<EventResponse> events = eventService.listEvents();

        return ResponseEntity.ok(events);
    }

}
