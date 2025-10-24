package com.example.eventsync.service;

import com.example.eventsync.dto.CreateEventRequest;
import com.example.eventsync.dto.EventResponse;
import com.example.eventsync.entity.Event;
import com.example.eventsync.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final EventRepository eventRepository;

    public EventResponse createEvent(CreateEventRequest request) {
        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .build();

        Event saved = eventRepository.save(event);

        return EventResponse.builder()
                .id(saved.getId())
                .title(saved.getTitle())
                .description(saved.getDescription())
                .build();
    }

    public List<EventResponse> listEvents() {
        List<Event> events = eventRepository.findAll();

        return events.stream()
                .map(event -> EventResponse.builder()
                        .id(event.getId())
                        .title(event.getTitle())
                        .description(event.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
