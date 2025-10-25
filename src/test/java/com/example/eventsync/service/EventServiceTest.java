package com.example.eventsync.service;

import com.example.eventsync.dto.CreateEventRequest;
import com.example.eventsync.dto.EventResponse;
import com.example.eventsync.entity.Event;
import com.example.eventsync.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void createEvent_ShouldSaveAndReturnEvent() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .title("Tech Conference 2025")
                .description("Annual technology conference")
                .build();

        Event savedEvent = Event.builder()
                .id(1L)
                .title("Tech Conference 2025")
                .description("Annual technology conference")
                .build();

        when(eventRepository.save(any(Event.class)))
                .thenReturn(savedEvent);

        // when
        EventResponse response = eventService.createEvent(request);

        // then
        assertThat(response)
                .isNotNull()
                .satisfies(r -> {
                    assertThat(r.getId()).isEqualTo(1L);
                    assertThat(r.getTitle()).isEqualTo("Tech Conference 2025");
                    assertThat(r.getDescription()).isEqualTo("Annual technology conference");
                });

        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void createEvent_WithNullDescription_ShouldSaveAndReturnEvent() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .title("Tech Conference 2025")
                .description(null)
                .build();

        Event savedEvent = Event.builder()
                .id(1L)
                .title("Tech Conference 2025")
                .description(null)
                .build();

        when(eventRepository.save(any(Event.class)))
                .thenReturn(savedEvent);

        // when
        EventResponse response = eventService.createEvent(request);

        // then
        assertThat(response)
                .isNotNull()
                .satisfies(r -> {
                    assertThat(r.getId()).isEqualTo(1L);
                    assertThat(r.getTitle()).isEqualTo("Tech Conference 2025");
                    assertThat(r.getDescription()).isNull();
                });

        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void listEvents_ShouldReturnAllEvents() {
        // given
        List<Event> events = Arrays.asList(
                Event.builder()
                        .id(1L)
                        .title("First Event")
                        .description("First event description")
                        .build(),
                Event.builder()
                        .id(2L)
                        .title("Second Event")
                        .description("Second event description")
                        .build()
        );

        when(eventRepository.findAll())
                .thenReturn(events);

        // when
        List<EventResponse> responses = eventService.listEvents();

        // then
        assertThat(responses)
                .hasSize(2)
                .satisfies(list -> {
                    assertThat(list.get(0))
                            .satisfies(r -> {
                                assertThat(r.getId()).isEqualTo(1L);
                                assertThat(r.getTitle()).isEqualTo("First Event");
                                assertThat(r.getDescription()).isEqualTo("First event description");
                            });
                    assertThat(list.get(1))
                            .satisfies(r -> {
                                assertThat(r.getId()).isEqualTo(2L);
                                assertThat(r.getTitle()).isEqualTo("Second Event");
                                assertThat(r.getDescription()).isEqualTo("Second event description");
                            });
                });

        verify(eventRepository).findAll();
    }

    @Test
    void listEvents_WhenNoEvents_ShouldReturnEmptyList() {
        // given
        when(eventRepository.findAll())
                .thenReturn(List.of());

        // when
        List<EventResponse> responses = eventService.listEvents();

        // then
        assertThat(responses).isEmpty();
        verify(eventRepository).findAll();
    }

    @Test
    void createEvent_ShouldMapAllFieldsCorrectly() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .title("Mapping Test")
                .description("Testing field mapping")
                .build();

        Event savedEvent = Event.builder()
                .id(1L)
                .title("Mapping Test")
                .description("Testing field mapping")
                .build();

        when(eventRepository.save(any(Event.class)))
                .thenAnswer(invocation -> {
                    Event eventToSave = invocation.getArgument(0);
                    assertThat(eventToSave)
                            .satisfies(e -> {
                                assertThat(e.getTitle()).isEqualTo(request.getTitle());
                                assertThat(e.getDescription()).isEqualTo(request.getDescription());
                            });
                    return savedEvent;
                });

        // when
        EventResponse response = eventService.createEvent(request);

        // then
        assertThat(response)
                .satisfies(r -> {
                    assertThat(r.getId()).isEqualTo(savedEvent.getId());
                    assertThat(r.getTitle()).isEqualTo(savedEvent.getTitle());
                    assertThat(r.getDescription()).isEqualTo(savedEvent.getDescription());
                });
    }
}