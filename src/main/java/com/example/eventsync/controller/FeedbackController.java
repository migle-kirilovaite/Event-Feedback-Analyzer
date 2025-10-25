package com.example.eventsync.controller;

import com.example.eventsync.dto.CreateFeedbackRequest;
import com.example.eventsync.dto.EventSentimentSummary;
import com.example.eventsync.dto.FeedbackResponse;
import com.example.eventsync.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(
        path = "/events/{eventId}",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;

    @Operation(summary = "Submit feedback for an event")
    @PostMapping("/feedback")
    public ResponseEntity<FeedbackResponse> submitFeedback(@PathVariable Long eventId, @Valid @RequestBody CreateFeedbackRequest request) {
        FeedbackResponse created = feedbackService.submitFeedback(eventId, request);
        URI location = URI.create(String.format("/events/%s/feedback/%s", eventId, created.getId()));

        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Get sentiment analysis summary of an event")
    @GetMapping("/summary")
        public ResponseEntity<EventSentimentSummary> getEventSummary(@PathVariable Long eventId) {
        EventSentimentSummary summary = feedbackService.getEventSentimentSummary(eventId);

        return ResponseEntity.ok(summary);
    }
}
