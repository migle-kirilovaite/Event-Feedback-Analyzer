package com.example.eventsync.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class EventTest {

    @Test
    void builderPattern_ShouldCreateEventWithAllFields() {
        // given
        Long id = 1L;
        String title = "Test Event";
        String description = "Test Description";

        // when
        Event event = Event.builder()
                .id(id)
                .title(title)
                .description(description)
                .build();

        // then
        assertThat(event.getId()).isEqualTo(id);
        assertThat(event.getTitle()).isEqualTo(title);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // given
        Event event = new Event();

        // when
        event.setId(1L);
        event.setTitle("Updated Title");
        event.setDescription("Updated Description");

        // then
        assertThat(event.getId()).isEqualTo(1L);
        assertThat(event.getTitle()).isEqualTo("Updated Title");
        assertThat(event.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void allArgsConstructor_ShouldCreateEventWithAllFields() {
        // given
        Long id = 1L;
        String title = "Test Event";
        String description = "Test Description";

        // when
        Event event = new Event(id, title, description);

        // then
        assertThat(event.getId()).isEqualTo(id);
        assertThat(event.getTitle()).isEqualTo(title);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyEvent() {
        // when
        Event event = new Event();

        // then
        assertThat(event.getId()).isNull();
        assertThat(event.getTitle()).isNull();
        assertThat(event.getDescription()).isNull();
    }

    @Test
    void builder_ShouldAllowNullDescription() {
        // when
        Event event = Event.builder()
                .title("Test Event")
                .build();

        // then
        assertThat(event.getTitle()).isNotNull();
        assertThat(event.getDescription()).isNull();
    }
}