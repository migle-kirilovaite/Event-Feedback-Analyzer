package com.example.eventsync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Feedback response details")
public class FeedbackResponse {
    @Schema(description = "Unique identifier of the feedback", example = "1")
    private Long id;

    @Schema(description = "ID of the event this feedback belongs to", example = "1")
    private Long eventId;

    @Schema(description = "Feedback text content", example = "Great event, really enjoyed the presentations!")
    private String text;

    @Schema(description = "Timestamp when the feedback was created",
            example = "2025-10-25T12:26:45Z",
            pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Instant createdAt;
}
