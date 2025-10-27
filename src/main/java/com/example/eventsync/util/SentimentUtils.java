package com.example.eventsync.util;

import com.example.eventsync.dto.SentimentStats;

import java.util.Locale;

public class SentimentUtils {

    public static String mapLabelToSentiment(String label) {
        if (label == null) return "neutral";
        String s = label.toLowerCase(Locale.ROOT);
        if (s.equals("label_2")) return "positive";
        if (s.equals("label_0")) return "negative";
        return "neutral";
    }

    public static SentimentStats makeStats(int count, double sum) {
        if (count == 0) return SentimentStats.builder().count(0).avgScore(0.0).build();
        return SentimentStats.builder().count(count).avgScore(sum / count).build();
    }
}
