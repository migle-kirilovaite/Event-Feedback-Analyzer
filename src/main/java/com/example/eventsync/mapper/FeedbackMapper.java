package com.example.eventsync.mapper;

import com.example.eventsync.dto.FeedbackResponse;
import com.example.eventsync.entity.Feedback;

public class FeedbackMapper {

    public static FeedbackResponse toDto(Feedback feedback) {
        return FeedbackResponse.builder()
                .id(feedback.getId())
                .eventId(feedback.getEvent().getId())
                .text(feedback.getText())
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
