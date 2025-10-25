package com.example.eventsync.service;

import com.example.eventsync.dto.CreateFeedbackRequest;
import com.example.eventsync.dto.FeedbackResponse;
import com.example.eventsync.dto.EventSentimentSummary;
import com.example.eventsync.dto.SentimentStats;
import com.example.eventsync.entity.Event;
import com.example.eventsync.entity.Feedback;
import com.example.eventsync.repository.EventRepository;
import com.example.eventsync.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {
    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${huggingface.api.token:${HUGGING_FACE_API_TOKEN:}}")
    private String huggingFaceToken;

    @Value("${huggingface.model:cardiffnlp/twitter-roberta-base-sentiment}")
    private String huggingFaceModel;

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

            Map<String, Object> result = analyzeTextWithHuggingFace(text);
            String label = null;
            double score = 0.0;
            if (result != null) {
                Object l = result.get("label");
                Object s = result.get("score");
                if (l instanceof String) label = (String) l;
                if (s instanceof Number) score = ((Number) s).doubleValue();
            }

            String sentiment = mapLabelToSentiment(label);

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

        SentimentStats posStats = makeStats(posCount, posSum);
        SentimentStats neuStats = makeStats(neuCount, neuSum);
        SentimentStats negStats = makeStats(negCount, negSum);

        int total = posCount + neuCount + negCount;

        return EventSentimentSummary.builder()
                .eventId(eventId)
                .totalFeedback(total)
                .positive(posStats)
                .neutral(neuStats)
                .negative(negStats)
                .build();
    }

    public Map<String, Object> analyzeTextWithHuggingFace(String text) {
        WebClient client = WebClient.builder()
                .baseUrl("https://router.huggingface.co")
                .build();

        try {
            List<List<Map<String, Object>>>  response = client.post()
                    .uri("/hf-inference/models/" + huggingFaceModel)
                    .header("Authorization", "Bearer " + huggingFaceToken)
                    .header("Content-Type", "application/json")
                    .bodyValue(Map.of("inputs", text))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<List<Map<String,Object>>>>() {})
                    .block();

            if (response != null && !response.isEmpty() && response.get(0) != null && !response.get(0).isEmpty()) {
                Map<String, Object> first = response.get(0).get(0);
                return first;
            }

            Map<String, Object> fallback = new HashMap<>();
            fallback.put("label", "neutral");
            fallback.put("score", 0.0);
            return fallback;

        } catch (WebClientResponseException ex) {
            String body = ex.getResponseBodyAsString();
            throw new ResponseStatusException(ex.getStatusCode(),
                    "HuggingFace error: " + (body != null ? body : ex.getMessage()));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Failed to call HuggingFace: " + ex.getMessage(), ex);
        }
    }

    public static String mapLabelToSentiment(String label) {
        if (label == null) return "neutral";
        String s = label.toLowerCase(Locale.ROOT);
        if (s.equals("label_2")) return "positive";
        if (s.equals("label_0")) return "negative";

        return "neutral";
    }

    public static SentimentStats makeStats(int count, double sum) {
        if (count == 0)
            return SentimentStats.builder().count(0).avgScore(0.0).build();
        double avg = sum / count;

        return SentimentStats.builder().count(count).avgScore(avg).build();
    }

}