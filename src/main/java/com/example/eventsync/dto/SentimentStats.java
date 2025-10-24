package com.example.eventsync.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentimentStats {
    private int count;
    private double avgScore;
}
