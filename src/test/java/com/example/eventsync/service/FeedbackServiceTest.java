package com.example.eventsync.service;

import com.example.eventsync.dto.CreateFeedbackRequest;
import com.example.eventsync.dto.EventSentimentSummary;
import com.example.eventsync.dto.FeedbackResponse;
import com.example.eventsync.dto.SentimentStats;
import com.example.eventsync.entity.Event;
import com.example.eventsync.entity.Feedback;
import com.example.eventsync.repository.EventRepository;
import com.example.eventsync.repository.FeedbackRepository;
import com.example.eventsync.util.SentimentUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    private static final Instant FIXED_TIME = Instant.parse("2025-10-25T11:00:59Z");

    @Test
    void submitFeedback_ShouldCreateAndReturnFeedback() {
        // given
        Long eventId = 1L;
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text("Great event!")
                .build();

        Event event = Event.builder()
                .id(eventId)
                .title("Test Event")
                .build();

        Feedback savedFeedback = Feedback.builder()
                .id(1L)
                .event(event)
                .text("Great event!")
                .createdAt(FIXED_TIME)
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(savedFeedback);

        // when
        FeedbackResponse response = feedbackService.submitFeedback(eventId, request);

        // then
        assertThat(response)
                .isNotNull()
                .satisfies(r -> {
                    assertThat(r.getId()).isEqualTo(1L);
                    assertThat(r.getEventId()).isEqualTo(eventId);
                    assertThat(r.getText()).isEqualTo("Great event!");
                    assertThat(r.getCreatedAt()).isEqualTo(FIXED_TIME);
                });

        verify(eventRepository).findById(eventId);
        verify(feedbackRepository).save(any(Feedback.class));
    }

    @Test
    void submitFeedback_WhenEventNotFound_ShouldThrowException() {
        // given
        Long eventId = 1L;
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text("Great event!")
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> feedbackService.submitFeedback(eventId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Event not found");
    }

    @Test
    void getEventSentimentSummary_WhenEventNotFound_ShouldThrowException() {
        // given
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> feedbackService.getEventSentimentSummary(eventId))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Event not found");
    }

    @ParameterizedTest
    @ValueSource(strings = {"label_2", "label_0", "label_1", "something_else"})
    @NullAndEmptySource
     void mapLabelToSentiment_ShouldMapCorrectly(String label) {
        // when
        String sentiment = SentimentUtils.mapLabelToSentiment(label);

        // then
        assertThat(sentiment)
                .isNotNull()
                .satisfies(s -> {
                    if ("label_2".equals(label)) {
                        assertThat(s).isEqualTo("positive");
                    } else if ("label_0".equals(label)) {
                        assertThat(s).isEqualTo("negative");
                    } else {
                        assertThat(s).isEqualTo("neutral");
                    }
                });
    }

    @Test
    void makeStats_WhenCountIsZero_ShouldReturnZeroStats() {
        // when
        SentimentStats stats = SentimentUtils.makeStats(0, 0.0);

        // then
        assertThat(stats)
                .isNotNull()
                .satisfies(s -> {
                    assertThat(s.getCount()).isZero();
                    assertThat(s.getAvgScore()).isZero();
                });
    }

    @Test
    void makeStats_WhenHasValues_ShouldCalculateAverage() {
        // when
        SentimentStats stats = SentimentUtils.makeStats(2, 1.5);

        // then
        assertThat(stats)
                .isNotNull()
                .satisfies(s -> {
                    assertThat(s.getCount()).isEqualTo(2);
                    assertThat(s.getAvgScore()).isEqualTo(0.75); // 1.5/2
                });
    }

    @Test
    void getEventSentimentSummary_WithEmptyText_ShouldCountAsNeutral() {
        // given
        Long eventId = 1L;
        Event event = Event.builder().id(eventId).build();
        Feedback feedback = Feedback.builder()
                .id(1L)
                .event(event)
                .text("")
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(feedbackRepository.findByEventId(eventId)).thenReturn(List.of(feedback));

        // when
        EventSentimentSummary summary = feedbackService.getEventSentimentSummary(eventId);

        // then
        assertThat(summary)
                .isNotNull()
                .satisfies(s -> {
                    assertThat(s.getTotalFeedback()).isEqualTo(1);
                    assertThat(s.getNeutral().getCount()).isEqualTo(1);
                    assertThat(s.getPositive().getCount()).isZero();
                    assertThat(s.getNegative().getCount()).isZero();
                });
    }
}