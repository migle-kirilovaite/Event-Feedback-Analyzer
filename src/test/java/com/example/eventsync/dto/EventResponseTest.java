package com.example.eventsync.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class EventResponseTest {

    @Test
    void builderPattern_ShouldCreateResponseWithAllFields() {
        // given
        Long id = 1L;
        String title = "Test Event";
        String description = "Test Description";

        // when
        EventResponse response = EventResponse.builder()
                .id(id)
                .title(title)
                .description(description)
                .build();

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getDescription()).isEqualTo(description);
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // given
        EventResponse response = new EventResponse();
        Long id = 1L;
        String title = "Updated Event";
        String description = "Updated Description";

        // when
        response.setId(id);
        response.setTitle(title);
        response.setDescription(description);

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getDescription()).isEqualTo(description);
    }

    @Test
    void allArgsConstructor_ShouldCreateResponseWithAllFields() {
        // given
        Long id = 1L;
        String title = "Test Event";
        String description = "Test Description";

        // when
        EventResponse response = new EventResponse(id, title, description);

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getDescription()).isEqualTo(description);
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyResponse() {
        // when
        EventResponse response = new EventResponse();

        // then
        assertThat(response.getId()).isNull();
        assertThat(response.getTitle()).isNull();
        assertThat(response.getDescription()).isNull();
    }

    @Test
    void builder_WithNullDescription_ShouldCreateValidResponse() {
        // when
        EventResponse response = EventResponse.builder()
                .id(1L)
                .title("Event Title")
                .description(null)
                .build();

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Event Title");
        assertThat(response.getDescription()).isNull();
    }

    @Test
    void builder_WithMinimalFields_ShouldCreateValidResponse() {
        // when
        EventResponse response = EventResponse.builder()
                .id(1L)
                .build();

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isNull();
        assertThat(response.getDescription()).isNull();
    }

    @Test
    void setters_WithNullValues_ShouldWork() {
        // given
        EventResponse response = EventResponse.builder()
                .id(1L)
                .title("Initial Title")
                .description("Initial Description")
                .build();

        // when
        response.setTitle(null);
        response.setDescription(null);

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isNull();
        assertThat(response.getDescription()).isNull();
    }
}