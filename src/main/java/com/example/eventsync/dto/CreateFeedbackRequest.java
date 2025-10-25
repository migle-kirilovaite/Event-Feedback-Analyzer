package com.example.eventsync.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Feedback submission request")
public class CreateFeedbackRequest {
    @NotBlank(message = "Feedback text must not be blank")
    @Size(max = 2000, message = "Feedback text must not exceed 2000 characters")
    @Schema(description = "Feedback text content",
            example = "Great event! The speakers were very knowledgeable and engaging.",
            minLength = 1,
            maxLength = 2000,
            required = true)
    private String text;
}
