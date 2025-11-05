package com.example.eventsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Event creation request")
public class CreateEventRequest {
    @NotBlank(message = "Title must not be blank")
    @Size(max = 64, message = "Title must not exceed 64 characters")
    @Schema(description = "Event title",
            example = "Tech Conference 2025",
            minLength = 1,
            maxLength = 255,
            required = true)
    private String title;

    @Size(max = 256, message = "Description must not exceed 256 characters")
    @Schema(description = "Event description",
            example = "Annual technology conference with industry experts",
            maxLength = 1000)
    private String description;
}
