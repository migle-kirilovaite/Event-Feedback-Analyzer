package com.example.eventsync.controller;

import com.example.eventsync.dto.CreateEventRequest;
import com.example.eventsync.dto.EventResponse;
import com.example.eventsync.service.EventService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    void createEvent_ShouldReturnCreatedResponse() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .title("Tech Conference 2025")
                .description("Annual technology conference")
                .build();

        EventResponse mockResponse = EventResponse.builder()
                .id(1L)
                .title("Tech Conference 2025")
                .description("Annual technology conference")
                .build();

        when(eventService.createEvent(any(CreateEventRequest.class)))
                .thenReturn(mockResponse);

        // when
        ResponseEntity<EventResponse> response = eventController.createdEvent(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(mockResponse);
        assertThat(response.getHeaders().getLocation())
                .hasToString("/events/1");
    }

    @Test
    void createEvent_ShouldCreateCorrectLocationHeader() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .title("Workshop Event")
                .description("Hands-on workshop")
                .build();

        EventResponse mockResponse = EventResponse.builder()
                .id(123L)
                .title("Workshop Event")
                .description("Hands-on workshop")
                .build();

        when(eventService.createEvent(any(CreateEventRequest.class)))
                .thenReturn(mockResponse);

        // when
        ResponseEntity<EventResponse> response = eventController.createdEvent(request);

        // then
        assertThat(response.getHeaders().getLocation())
                .hasToString("/events/123");
    }

    @Test
    void createEvent_ShouldReturnCreatedEvent() {
        // given
        String title = "Team Building";
        String description = "Annual team building event";

        CreateEventRequest request = CreateEventRequest.builder()
                .title(title)
                .description(description)
                .build();

        EventResponse mockResponse = EventResponse.builder()
                .id(1L)
                .title(title)
                .description(description)
                .build();

        when(eventService.createEvent(any(CreateEventRequest.class)))
                .thenReturn(mockResponse);

        // when
        ResponseEntity<EventResponse> response = eventController.createdEvent(request);

        // then
        assertThat(response.getBody())
                .isNotNull()
                .satisfies(body -> {
                    assertThat(body.getId()).isEqualTo(1L);
                    assertThat(body.getTitle()).isEqualTo(title);
                    assertThat(body.getDescription()).isEqualTo(description);
                });
    }

    @Test
    void listEvents_ShouldReturnAllEvents() {
        // given
        List<EventResponse> mockEvents = Arrays.asList(
                EventResponse.builder()
                        .id(1L)
                        .title("First Event")
                        .description("First event description")
                        .build(),
                EventResponse.builder()
                        .id(2L)
                        .title("Second Event")
                        .description("Second event description")
                        .build()
        );

        when(eventService.listEvents())
                .thenReturn(mockEvents);

        // when
        ResponseEntity<List<EventResponse>> response = eventController.listEvents();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .hasSize(2)
                .containsExactlyElementsOf(mockEvents);
    }

    @Test
    void listEvents_WhenNoEvents_ShouldReturnEmptyList() {
        // given
        List<EventResponse> emptyList = List.of();
        when(eventService.listEvents())
                .thenReturn(emptyList);

        // when
        ResponseEntity<List<EventResponse>> response = eventController.listEvents();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .isEmpty();
    }
}