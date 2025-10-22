package com.example.eventsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFeedbackRequest {
    @NotBlank(message = "Feedback text must not be blank")
    @Size(max = 2000, message = "Feedback text must not exceed 2000 characters")
    private String text;
}
