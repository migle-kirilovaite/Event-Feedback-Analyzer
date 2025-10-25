package com.example.eventsync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Statistics for a specific sentiment category")
public class SentimentStats {
    @Schema(description = "Number of feedback items in this sentiment category",
            example = "5",
            minimum = "0")
    private int count;
    @Schema(description = "Average sentiment score for this category",
            example = "0.85",
            minimum = "0",
            maximum = "1")
    private double avgScore;
}
