package com.example.eventsync.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class EventSentimentSummaryTest {

    @Test
    void builderPattern_ShouldCreateSummaryWithAllFields() {
        // given
        Long eventId = 1L;
        int totalFeedback = 10;
        SentimentStats positive = new SentimentStats();
        SentimentStats negative = new SentimentStats();
        SentimentStats neutral = new SentimentStats();

        // when
        EventSentimentSummary summary = EventSentimentSummary.builder()
                .eventId(eventId)
                .totalFeedback(totalFeedback)
                .positive(positive)
                .negative(negative)
                .neutral(neutral)
                .build();

        // then
        assertThat(summary.getEventId()).isEqualTo(eventId);
        assertThat(summary.getTotalFeedback()).isEqualTo(totalFeedback);
        assertThat(summary.getPositive()).isEqualTo(positive);
        assertThat(summary.getNegative()).isEqualTo(negative);
        assertThat(summary.getNeutral()).isEqualTo(neutral);
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // given
        EventSentimentSummary summary = new EventSentimentSummary();
        Long eventId = 1L;
        int totalFeedback = 5;
        SentimentStats positive = new SentimentStats();
        SentimentStats negative = new SentimentStats();
        SentimentStats neutral = new SentimentStats();

        // when
        summary.setEventId(eventId);
        summary.setTotalFeedback(totalFeedback);
        summary.setPositive(positive);
        summary.setNegative(negative);
        summary.setNeutral(neutral);

        // then
        assertThat(summary.getEventId()).isEqualTo(eventId);
        assertThat(summary.getTotalFeedback()).isEqualTo(totalFeedback);
        assertThat(summary.getPositive()).isEqualTo(positive);
        assertThat(summary.getNegative()).isEqualTo(negative);
        assertThat(summary.getNeutral()).isEqualTo(neutral);
    }

    @Test
    void allArgsConstructor_ShouldCreateSummaryWithAllFields() {
        // given
        Long eventId = 1L;
        int totalFeedback = 15;
        SentimentStats positive = new SentimentStats();
        SentimentStats negative = new SentimentStats();
        SentimentStats neutral = new SentimentStats();

        // when
        EventSentimentSummary summary = new EventSentimentSummary(
                eventId,
                totalFeedback,
                positive,
                negative,
                neutral
        );

        // then
        assertThat(summary.getEventId()).isEqualTo(eventId);
        assertThat(summary.getTotalFeedback()).isEqualTo(totalFeedback);
        assertThat(summary.getPositive()).isEqualTo(positive);
        assertThat(summary.getNegative()).isEqualTo(negative);
        assertThat(summary.getNeutral()).isEqualTo(neutral);
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptySummary() {
        // when
        EventSentimentSummary summary = new EventSentimentSummary();

        // then
        assertThat(summary.getEventId()).isNull();
        assertThat(summary.getTotalFeedback()).isEqualTo(0);
        assertThat(summary.getPositive()).isNull();
        assertThat(summary.getNegative()).isNull();
        assertThat(summary.getNeutral()).isNull();
    }

    @Test
    void builder_WithNullStats_ShouldCreateValidSummary() {
        // when
        EventSentimentSummary summary = EventSentimentSummary.builder()
                .eventId(1L)
                .totalFeedback(0)
                .build();

        // then
        assertThat(summary.getEventId()).isEqualTo(1L);
        assertThat(summary.getTotalFeedback()).isEqualTo(0);
        assertThat(summary.getPositive()).isNull();
        assertThat(summary.getNegative()).isNull();
        assertThat(summary.getNeutral()).isNull();
    }

    @Test
    void builder_WithPartialStats_ShouldCreateValidSummary() {
        // given
        SentimentStats positive = new SentimentStats();
        SentimentStats negative = new SentimentStats();

        // when
        EventSentimentSummary summary = EventSentimentSummary.builder()
                .eventId(1L)
                .totalFeedback(2)
                .positive(positive)
                .negative(negative)
                .build();

        // then
        assertThat(summary.getEventId()).isEqualTo(1L);
        assertThat(summary.getTotalFeedback()).isEqualTo(2);
        assertThat(summary.getPositive()).isEqualTo(positive);
        assertThat(summary.getNegative()).isEqualTo(negative);
        assertThat(summary.getNeutral()).isNull();
    }

    @Test
    void setTotalFeedback_WithNegativeValue_ShouldAllowNegativeValue() {
        // given
        EventSentimentSummary summary = new EventSentimentSummary();

        // when
        summary.setTotalFeedback(-1);

        // then
        assertThat(summary.getTotalFeedback()).isEqualTo(-1);
    }

    @Test
    void setters_WithNullValues_ShouldWork() {
        // given
        EventSentimentSummary summary = EventSentimentSummary.builder()
                .eventId(1L)
                .totalFeedback(5)
                .positive(new SentimentStats())
                .negative(new SentimentStats())
                .neutral(new SentimentStats())
                .build();

        // when
        summary.setEventId(null);
        summary.setPositive(null);
        summary.setNegative(null);
        summary.setNeutral(null);

        // then
        assertThat(summary.getEventId()).isNull();
        assertThat(summary.getTotalFeedback()).isEqualTo(5);
        assertThat(summary.getPositive()).isNull();
        assertThat(summary.getNegative()).isNull();
        assertThat(summary.getNeutral()).isNull();
    }
}