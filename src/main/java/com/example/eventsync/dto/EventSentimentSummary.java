package com.example.eventsync.dto;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Summary of sentiment analysis for event feedback")
public class EventSentimentSummary {
    @Schema(description = "Unique identifier of the event", example = "1")
    private Long eventId;
    @Schema(description = "Total number of feedback submissions", example = "10")
    private int totalFeedback;
    @Schema(description = "Statistics for positive sentiment feedback")
    private SentimentStats positive;
    @Schema(description = "Statistics for negative sentiment feedback")
    private SentimentStats negative;
    @Schema(description = "Statistics for neutral sentiment feedback")
    private SentimentStats neutral;
}
