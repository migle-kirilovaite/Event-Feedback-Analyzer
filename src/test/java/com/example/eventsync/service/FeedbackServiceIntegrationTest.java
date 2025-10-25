package com.example.eventsync.service;

import com.example.eventsync.dto.CreateEventRequest;
import com.example.eventsync.dto.CreateFeedbackRequest;
import com.example.eventsync.dto.EventResponse;
import com.example.eventsync.dto.EventSentimentSummary;
import com.example.eventsync.dto.FeedbackResponse;
import com.example.eventsync.entity.Event;
import com.example.eventsync.repository.EventRepository;
import com.example.eventsync.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class FeedbackServiceIntegrationTest {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    private EventResponse testEvent;

    @BeforeEach
    void setUp() {
        feedbackRepository.deleteAll();
        eventRepository.deleteAll();

        // Create a test event
        CreateEventRequest eventRequest = CreateEventRequest.builder()
                .title("Integration Test Event")
                .description("Event for integration testing")
                .build();

        testEvent = eventService.createEvent(eventRequest);
    }

    @Test
    void submitFeedback_ShouldCreateAndReturnFeedback() {
        // given
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text("This is a test feedback")
                .build();

        Instant beforeSubmission = Instant.now();

        // when
        FeedbackResponse response = feedbackService.submitFeedback(testEvent.getId(), request);

        // then
        assertThat(response)
                .isNotNull()
                .satisfies(r -> {
                    assertThat(r.getId()).isNotNull();
                    assertThat(r.getEventId()).isEqualTo(testEvent.getId());
                    assertThat(r.getText()).isEqualTo("This is a test feedback");
                    assertThat(r.getCreatedAt())
                            .isNotNull()
                            .isBetween(
                                    beforeSubmission,
                                    beforeSubmission.plus(Duration.ofSeconds(5))
                            );
                });

        // Verify the feedback is in the database
        assertThat(feedbackRepository.findById(response.getId()))
                .isPresent()
                .hasValueSatisfying(feedback -> {
                    assertThat(feedback.getText()).isEqualTo("This is a test feedback");
                    assertThat(feedback.getEvent().getId()).isEqualTo(testEvent.getId());
                    assertThat(feedback.getCreatedAt())
                            .isNotNull()
                            .isBetween(
                                    beforeSubmission,
                                    beforeSubmission.plus(Duration.ofSeconds(5))
                            );
                });
    }

    @Test
    void submitFeedback_WhenEventDoesNotExist_ShouldThrowException() {
        // given
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text("This is a test feedback")
                .build();

        Long nonExistentEventId = 99999L;

        // when/then
        assertThatThrownBy(() -> feedbackService.submitFeedback(nonExistentEventId, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Event not found");
    }

    @Test
    void getEventSentimentSummary_ShouldReturnSummaryWithSentiments() {
        // given
        // Submit multiple feedbacks with different sentiments
        submitFeedback("This is amazing! Really enjoyed it!");  // positive
        submitFeedback("This was terrible, very disappointed."); // negative
        submitFeedback("It was okay, nothing special.");        // neutral

        // when
        EventSentimentSummary summary = feedbackService.getEventSentimentSummary(testEvent.getId());

        // then
        assertThat(summary)
                .isNotNull()
                .satisfies(s -> {
                    assertThat(s.getEventId()).isEqualTo(testEvent.getId());
                    assertThat(s.getTotalFeedback()).isEqualTo(3);

                    // Check that we have sentiment stats
                    assertThat(s.getPositive()).isNotNull();
                    assertThat(s.getNegative()).isNotNull();
                    assertThat(s.getNeutral()).isNotNull();

                    // Verify total counts add up
                    int totalCount = s.getPositive().getCount() +
                            s.getNegative().getCount() +
                            s.getNeutral().getCount();
                    assertThat(totalCount).isEqualTo(3);
                });
    }

    @Test
    void getEventSentimentSummary_WhenNoFeedback_ShouldReturnEmptySummary() {
        // when
        EventSentimentSummary summary = feedbackService.getEventSentimentSummary(testEvent.getId());

        // then
        assertThat(summary)
                .isNotNull()
                .satisfies(s -> {
                    assertThat(s.getEventId()).isEqualTo(testEvent.getId());
                    assertThat(s.getTotalFeedback()).isZero();
                    assertThat(s.getPositive().getCount()).isZero();
                    assertThat(s.getNegative().getCount()).isZero();
                    assertThat(s.getNeutral().getCount()).isZero();
                });
    }

    @Test
    void getEventSentimentSummary_WhenEventDoesNotExist_ShouldThrowException() {
        // when/then
        assertThatThrownBy(() -> feedbackService.getEventSentimentSummary(99999L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Event not found");
    }

    private FeedbackResponse submitFeedback(String text) {
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text(text)
                .build();
        return feedbackService.submitFeedback(testEvent.getId(), request);
    }
}