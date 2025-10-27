package com.example.eventsync.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HuggingFaceClient {

    private final WebClient.Builder webClientBuilder;
    @Value("${huggingface.api.token}")
    private String token;

    public Map<String, Object> analyzeText(String text) {
        WebClient client = webClientBuilder.baseUrl("https://router.huggingface.co").build();

        try {
            List<List<Map<String, Object>>> response = client.post()
                    .uri("/hf-inference/models/cardiffnlp/twitter-roberta-base-sentiment")
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .bodyValue(Map.of("inputs", text))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<List<Map<String, Object>>>>() {})
                    .block();

            if (response != null && !response.isEmpty() && response.get(0) != null && !response.get(0).isEmpty()) {
                return response.get(0).get(0);
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
}
