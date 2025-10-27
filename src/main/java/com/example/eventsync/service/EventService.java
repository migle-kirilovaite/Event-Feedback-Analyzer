package com.example.eventsync.service;

import com.example.eventsync.dto.CreateEventRequest;
import com.example.eventsync.dto.EventResponse;
import com.example.eventsync.entity.Event;
import com.example.eventsync.mapper.EventMapper;
import com.example.eventsync.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {
    private final EventRepository eventRepository;

    public EventResponse createEvent(CreateEventRequest request) {
        Event event = EventMapper.toEntity(request);
        Event saved = eventRepository.save(event);

        return EventMapper.toDto(saved);
    }

    public List<EventResponse> listEvents() {
        return EventMapper.toDtoList(eventRepository.findAll());
    }
}
