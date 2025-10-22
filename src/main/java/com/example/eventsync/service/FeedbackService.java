package com.example.eventsync.service;

import com.example.eventsync.dto.CreateFeedbackRequest;
import com.example.eventsync.dto.FeedbackResponse;
import com.example.eventsync.entity.Feedback;
import com.example.eventsync.repository.FeedbackRepository;
import com.example.eventsync.repository.EventRepository;
import com.example.eventsync.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {
    private final FeedbackRepository feedbackRepository;
    private final EventRepository eventRepository;

    public FeedbackResponse submitFeedback(Long eventId, CreateFeedbackRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Feedback feedback = Feedback.builder()
                .event(event)
                .text(request.getText())
                .build();

        Feedback saved = feedbackRepository.save(feedback);

        return FeedbackResponse.builder()
                .id(saved.getId())
                .eventId(saved.getEvent().getId())
                .text(saved.getText())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
