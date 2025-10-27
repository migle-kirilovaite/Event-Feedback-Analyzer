package com.example.eventsync.service;

import com.example.eventsync.client.HuggingFaceClient;
import com.example.eventsync.dto.CreateFeedbackRequest;
import com.example.eventsync.dto.FeedbackResponse;
import com.example.eventsync.dto.EventSentimentSummary;
import com.example.eventsync.dto.SentimentStats;
import com.example.eventsync.entity.Event;
import com.example.eventsync.entity.Feedback;
import com.example.eventsync.mapper.FeedbackMapper;
import com.example.eventsync.repository.EventRepository;
import com.example.eventsync.repository.FeedbackRepository;

import com.example.eventsync.util.SentimentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {
    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;
    private final HuggingFaceClient huggingFaceClient;

    public FeedbackResponse submitFeedback(Long eventId, CreateFeedbackRequest request) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Feedback feedback = Feedback.builder()
                .event(event)
                .text(request.getText())
                .build();

        Feedback saved = feedbackRepository.save(feedback);

        return FeedbackMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public EventSentimentSummary getEventSentimentSummary(Long eventId) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));

        List<Feedback> feedbacks = feedbackRepository.findByEventId(eventId);

        int posCount = 0;
        double posSum = 0.0;
        int neuCount = 0;
        double neuSum = 0.0;
        int negCount = 0;
        double negSum = 0.0;

        for (Feedback fb : feedbacks) {
            String text = fb.getText();
            if (text == null || text.isBlank()) {
                neuCount++;
                neuSum += 0.0;
                continue;
            }

            Map<String, Object> result = huggingFaceClient.analyzeText(text);
            String label = null;
            double score = 0.0;
            if (result != null) {
                Object l = result.get("label");
                Object s = result.get("score");
                if (l instanceof String) label = (String) l;
                if (s instanceof Number) score = ((Number) s).doubleValue();
            }

            String sentiment = SentimentUtils.mapLabelToSentiment(label);

            switch (sentiment) {
                case "positive" -> {
                    posCount++;
                    posSum += score;
                }
                case "negative" -> {
                    negCount++;
                    negSum += score;
                }
                default -> {
                    neuCount++;
                    neuSum += score;
                }
            }
        }

        SentimentStats posStats = SentimentUtils.makeStats(posCount, posSum);
        SentimentStats neuStats = SentimentUtils.makeStats(neuCount, neuSum);
        SentimentStats negStats = SentimentUtils.makeStats(negCount, negSum);

        int total = posCount + neuCount + negCount;

        return EventSentimentSummary.builder()
                .eventId(eventId)
                .totalFeedback(total)
                .positive(posStats)
                .neutral(neuStats)
                .negative(negStats)
                .build();
    }

}