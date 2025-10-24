package com.example.eventsync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateEventRequest {
    @NotBlank(message = "Title must not be blank")
    @Size(max = 64, message = "Title must not exceed 64 characters")
    private String title;
    @Size(max = 256, message = "Description must not exceed 256 characters")
    private String description;
}
