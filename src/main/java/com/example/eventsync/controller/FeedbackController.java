package com.example.eventsync.controller;

import com.example.eventsync.dto.CreateFeedbackRequest;
import com.example.eventsync.dto.FeedbackResponse;
import com.example.eventsync.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/events/{eventId}")
@RequiredArgsConstructor
public class FeedbackController {
    private final FeedbackService feedbackService;
    @PostMapping("/feedback")
    public ResponseEntity<FeedbackResponse> submitFeedback(@PathVariable Long eventId, @Valid @RequestBody CreateFeedbackRequest request) {
        FeedbackResponse created = feedbackService.submitFeedback(eventId, request);
        URI location = URI.create(String.format("/events/%s/feedback/%s", eventId, created.getId()));
        return ResponseEntity.created(location).body(created);
    }
}
