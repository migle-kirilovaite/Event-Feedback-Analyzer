package com.example.eventsync.dto;

import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class FeedbackResponseTest {

    private static final Instant SAMPLE_INSTANT = Instant.parse("2025-10-25T10:37:01Z");

    @Test
    void builderPattern_ShouldCreateResponseWithAllFields() {
        // given
        Long id = 1L;
        Long eventId = 2L;
        String text = "Great event!";
        Instant createdAt = SAMPLE_INSTANT;

        // when
        FeedbackResponse response = FeedbackResponse.builder()
                .id(id)
                .eventId(eventId)
                .text(text)
                .createdAt(createdAt)
                .build();

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getEventId()).isEqualTo(eventId);
        assertThat(response.getText()).isEqualTo(text);
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // given
        FeedbackResponse response = new FeedbackResponse();
        Long id = 1L;
        Long eventId = 2L;
        String text = "Amazing experience!";
        Instant createdAt = SAMPLE_INSTANT;

        // when
        response.setId(id);
        response.setEventId(eventId);
        response.setText(text);
        response.setCreatedAt(createdAt);

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getEventId()).isEqualTo(eventId);
        assertThat(response.getText()).isEqualTo(text);
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void allArgsConstructor_ShouldCreateResponseWithAllFields() {
        // given
        Long id = 1L;
        Long eventId = 2L;
        String text = "Wonderful organization!";
        Instant createdAt = SAMPLE_INSTANT;

        // when
        FeedbackResponse response = new FeedbackResponse(id, eventId, text, createdAt);

        // then
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getEventId()).isEqualTo(eventId);
        assertThat(response.getText()).isEqualTo(text);
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyResponse() {
        // when
        FeedbackResponse response = new FeedbackResponse();

        // then
        assertThat(response.getId()).isNull();
        assertThat(response.getEventId()).isNull();
        assertThat(response.getText()).isNull();
        assertThat(response.getCreatedAt()).isNull();
    }

    @Test
    void builder_WithNullValues_ShouldCreateValidResponse() {
        // when
        FeedbackResponse response = FeedbackResponse.builder()
                .id(1L)
                .build();

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEventId()).isNull();
        assertThat(response.getText()).isNull();
        assertThat(response.getCreatedAt()).isNull();
    }

    @Test
    void builder_WithPartialData_ShouldCreateValidResponse() {
        // when
        FeedbackResponse response = FeedbackResponse.builder()
                .id(1L)
                .eventId(2L)
                .text("Partial feedback")
                .build();

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getEventId()).isEqualTo(2L);
        assertThat(response.getText()).isEqualTo("Partial feedback");
        assertThat(response.getCreatedAt()).isNull();
    }

    @Test
    void setters_WithNullValues_ShouldWork() {
        // given
        FeedbackResponse response = FeedbackResponse.builder()
                .id(1L)
                .eventId(2L)
                .text("Initial text")
                .createdAt(SAMPLE_INSTANT)
                .build();

        // when
        response.setId(null);
        response.setEventId(null);
        response.setText(null);
        response.setCreatedAt(null);

        // then
        assertThat(response.getId()).isNull();
        assertThat(response.getEventId()).isNull();
        assertThat(response.getText()).isNull();
        assertThat(response.getCreatedAt()).isNull();
    }

    @Test
    void createdAt_ShouldHandleDifferentInstants() {
        // given
        FeedbackResponse response = new FeedbackResponse();
        Instant pastInstant = Instant.parse("2024-01-01T00:00:00Z");
        Instant futureInstant = Instant.parse("2026-12-31T23:59:59Z");

        // when
        response.setCreatedAt(pastInstant);

        // then
        assertThat(response.getCreatedAt()).isEqualTo(pastInstant);

        // when
        response.setCreatedAt(futureInstant);

        // then
        assertThat(response.getCreatedAt()).isEqualTo(futureInstant);
    }
}