package com.example.eventsync.controller;

import com.example.eventsync.dto.CreateFeedbackRequest;
import com.example.eventsync.dto.EventSentimentSummary;
import com.example.eventsync.dto.FeedbackResponse;
import com.example.eventsync.dto.SentimentStats;
import com.example.eventsync.service.FeedbackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @InjectMocks
    private FeedbackController feedbackController;

    @Test
    void submitFeedback_ShouldReturnCreatedResponse() {
        // given
        Long eventId = 1L;
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text("Great event!")
                .build();

        FeedbackResponse mockResponse = FeedbackResponse.builder()
                .id(1L)
                .eventId(eventId)
                .text("Great event!")
                .createdAt(Instant.parse("2025-10-25T10:43:03Z"))
                .build();

        when(feedbackService.submitFeedback(eq(eventId), any(CreateFeedbackRequest.class)))
                .thenReturn(mockResponse);

        // when
        ResponseEntity<FeedbackResponse> response = feedbackController.submitFeedback(eventId, request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(mockResponse);
        assertThat(response.getHeaders().getLocation())
                .hasToString("/events/1/feedback/1");
    }

    @Test
    void getEventSummary_ShouldReturnOkResponse() {
        // given
        Long eventId = 1L;
        EventSentimentSummary mockSummary = EventSentimentSummary.builder()
                .eventId(eventId)
                .totalFeedback(5)
                .positive(new SentimentStats(2, 0.8))
                .negative(new SentimentStats(1, -0.6))
                .neutral(new SentimentStats(2, 0.1))
                .build();

        when(feedbackService.getEventSentimentSummary(eventId))
                .thenReturn(mockSummary);

        // when
        ResponseEntity<EventSentimentSummary> response = feedbackController.getEventSummary(eventId);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(mockSummary);
    }

    @Test
    void submitFeedback_ShouldCreateCorrectLocationHeader() {
        // given
        Long eventId = 123L;
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text("Excellent organization!")
                .build();

        FeedbackResponse mockResponse = FeedbackResponse.builder()
                .id(456L)
                .eventId(eventId)
                .text("Excellent organization!")
                .createdAt(Instant.parse("2025-10-25T10:43:03Z"))
                .build();

        when(feedbackService.submitFeedback(eq(eventId), any(CreateFeedbackRequest.class)))
                .thenReturn(mockResponse);

        // when
        ResponseEntity<FeedbackResponse> response = feedbackController.submitFeedback(eventId, request);

        // then
        assertThat(response.getHeaders().getLocation())
                .hasToString("/events/123/feedback/456");
    }

    @Test
    void submitFeedback_ShouldReturnSubmittedFeedback() {
        // given
        Long eventId = 1L;
        String feedbackText = "Very informative session!";
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text(feedbackText)
                .build();

        FeedbackResponse mockResponse = FeedbackResponse.builder()
                .id(1L)
                .eventId(eventId)
                .text(feedbackText)
                .createdAt(Instant.parse("2025-10-25T10:43:03Z"))
                .build();

        when(feedbackService.submitFeedback(eq(eventId), any(CreateFeedbackRequest.class)))
                .thenReturn(mockResponse);

        // when
        ResponseEntity<FeedbackResponse> response = feedbackController.submitFeedback(eventId, request);

        // then
        assertThat(response.getBody())
                .isNotNull()
                .satisfies(body -> {
                    assertThat(body.getId()).isEqualTo(1L);
                    assertThat(body.getEventId()).isEqualTo(eventId);
                    assertThat(body.getText()).isEqualTo(feedbackText);
                    assertThat(body.getCreatedAt()).isEqualTo(Instant.parse("2025-10-25T10:43:03Z"));
                });
    }

    @Test
    void getEventSummary_ShouldReturnCompleteSummary() {
        // given
        Long eventId = 1L;
        EventSentimentSummary mockSummary = EventSentimentSummary.builder()
                .eventId(eventId)
                .totalFeedback(10)
                .positive(new SentimentStats(5, 0.85))
                .negative(new SentimentStats(3, -0.7))
                .neutral(new SentimentStats(2, 0.2))
                .build();

        when(feedbackService.getEventSentimentSummary(eventId))
                .thenReturn(mockSummary);

        // when
        ResponseEntity<EventSentimentSummary> response = feedbackController.getEventSummary(eventId);

        // then
        assertThat(response.getBody())
                .isNotNull()
                .satisfies(summary -> {
                    assertThat(summary.getEventId()).isEqualTo(eventId);
                    assertThat(summary.getTotalFeedback()).isEqualTo(10);
                    assertThat(summary.getPositive().getCount()).isEqualTo(5);
                    assertThat(summary.getPositive().getAvgScore()).isEqualTo(0.85);
                    assertThat(summary.getNegative().getCount()).isEqualTo(3);
                    assertThat(summary.getNegative().getAvgScore()).isEqualTo(-0.7);
                    assertThat(summary.getNeutral().getCount()).isEqualTo(2);
                    assertThat(summary.getNeutral().getAvgScore()).isEqualTo(0.2);
                });
    }
}