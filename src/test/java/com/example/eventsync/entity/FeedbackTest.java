package com.example.eventsync.entity;

import org.junit.jupiter.api.Test;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class FeedbackTest {

    @Test
    void builderPattern_ShouldCreateFeedbackWithAllFields() {
        // given
        Long id = 1L;
        Event event = Event.builder()
                .id(1L)
                .title("Test Event")
                .build();
        String text = "Great event!";
        Instant createdAt = Instant.parse("2025-10-25T10:29:50Z");

        // when
        Feedback feedback = Feedback.builder()
                .id(id)
                .event(event)
                .text(text)
                .createdAt(createdAt)
                .build();

        // then
        assertThat(feedback.getId()).isEqualTo(id);
        assertThat(feedback.getEvent()).isEqualTo(event);
        assertThat(feedback.getText()).isEqualTo(text);
        assertThat(feedback.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // given
        Feedback feedback = new Feedback();
        Event event = Event.builder()
                .id(1L)
                .title("Test Event")
                .build();
        Instant now = Instant.now();

        // when
        feedback.setId(1L);
        feedback.setEvent(event);
        feedback.setText("Updated feedback text");
        feedback.setCreatedAt(now);

        // then
        assertThat(feedback.getId()).isEqualTo(1L);
        assertThat(feedback.getEvent()).isEqualTo(event);
        assertThat(feedback.getText()).isEqualTo("Updated feedback text");
        assertThat(feedback.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void allArgsConstructor_ShouldCreateFeedbackWithAllFields() {
        // given
        Long id = 1L;
        Event event = Event.builder()
                .id(1L)
                .title("Test Event")
                .build();
        String text = "Test feedback";
        Instant createdAt = Instant.parse("2025-10-25T10:29:50Z");

        // when
        Feedback feedback = new Feedback(id, event, text, createdAt);

        // then
        assertThat(feedback.getId()).isEqualTo(id);
        assertThat(feedback.getEvent()).isEqualTo(event);
        assertThat(feedback.getText()).isEqualTo(text);
        assertThat(feedback.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyFeedback() {
        // when
        Feedback feedback = new Feedback();

        // then
        assertThat(feedback.getId()).isNull();
        assertThat(feedback.getEvent()).isNull();
        assertThat(feedback.getText()).isNull();
        assertThat(feedback.getCreatedAt()).isNull();
    }

    @Test
    void prePersist_ShouldSetCreatedAtToCurrentTime() {
        // given
        Feedback feedback = new Feedback();
        Instant beforeTest = Instant.now();

        // when
        feedback.prePersist();
        Instant afterTest = Instant.now();

        // then
        assertThat(feedback.getCreatedAt())
                .isNotNull()
                .isBetween(beforeTest, afterTest);
    }

    @Test
    void builder_WithRequiredFieldsOnly_ShouldCreateValidFeedback() {
        // given
        Event event = Event.builder()
                .id(1L)
                .title("Test Event")
                .build();

        // when
        Feedback feedback = Feedback.builder()
                .event(event)
                .text("Required feedback text")
                .build();

        // then
        assertThat(feedback.getEvent()).isEqualTo(event);
        assertThat(feedback.getText()).isEqualTo("Required feedback text");
        assertThat(feedback.getId()).isNull();
        assertThat(feedback.getCreatedAt()).isNull();
    }

    @Test
    void setText_WithLongText_ShouldAcceptUpTo2000Characters() {
        // given
        Feedback feedback = new Feedback();
        String twoThousandChars = "a".repeat(2000);

        // when
        feedback.setText(twoThousandChars);

        // then
        assertThat(feedback.getText()).hasSize(2000);
        assertThat(feedback.getText()).isEqualTo(twoThousandChars);
    }
}