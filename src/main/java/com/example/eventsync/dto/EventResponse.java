package com.example.eventsync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Event response details")
public class EventResponse {
    @Schema(description = "Unique identifier of the event", example = "1")
    private Long id;
    @Schema(description = "Event title", example = "Tech Conference 2025")
    private String title;
    @Schema(description = "Event description", example = "Annual technology conference with industry experts")
    private String description;
}
