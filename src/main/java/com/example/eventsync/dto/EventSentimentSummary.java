package com.example.eventsync.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSentimentSummary {
    private Long eventId;
    private int totalFeedback;
    private SentimentStats positive;
    private SentimentStats negative;
    private SentimentStats neutral;
}
