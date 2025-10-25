package com.example.eventsync.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class SentimentStatsTest {

    @Test
    void builderPattern_ShouldCreateStatsWithAllFields() {
        // given
        int count = 10;
        double avgScore = 0.75;

        // when
        SentimentStats stats = SentimentStats.builder()
                .count(count)
                .avgScore(avgScore)
                .build();

        // then
        assertThat(stats.getCount()).isEqualTo(count);
        assertThat(stats.getAvgScore()).isEqualTo(avgScore);
    }

    @Test
    void builder_RequiresAllFields() {
        assertThatCode(() -> {
            SentimentStats.builder()
                    .count(1)
                    .avgScore(0.5)
                    .build();
        }).doesNotThrowAnyException();
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // given
        SentimentStats stats = new SentimentStats();
        int count = 5;
        double avgScore = 0.85;

        // when
        stats.setCount(count);
        stats.setAvgScore(avgScore);

        // then
        assertThat(stats.getCount()).isEqualTo(count);
        assertThat(stats.getAvgScore()).isEqualTo(avgScore);
    }

    @Test
    void allArgsConstructor_ShouldCreateStatsWithAllFields() {
        // given
        int count = 15;
        double avgScore = 0.95;

        // when
        SentimentStats stats = new SentimentStats(count, avgScore);

        // then
        assertThat(stats.getCount()).isEqualTo(count);
        assertThat(stats.getAvgScore()).isEqualTo(avgScore);
    }

    @Test
    void noArgsConstructor_ShouldCreateStatsWithDefaultValues() {
        // when
        SentimentStats stats = new SentimentStats();

        // then
        assertThat(stats.getCount()).isZero();
        assertThat(stats.getAvgScore()).isZero();
    }

    @Test
    void count_ShouldHandleNegativeValues() {
        // given
        SentimentStats stats = SentimentStats.builder()
                .count(-5)
                .avgScore(0.0)
                .build();

        // then
        assertThat(stats.getCount()).isEqualTo(-5);
    }

    @Test
    void avgScore_ShouldHandleExtremeValues() {
        // given
        SentimentStats smallValueStats = SentimentStats.builder()
                .count(1)
                .avgScore(0.000001)
                .build();

        // then
        assertThat(smallValueStats.getAvgScore()).isEqualTo(0.000001);

        // given
        SentimentStats largeValueStats = SentimentStats.builder()
                .count(1)
                .avgScore(999999.999999)
                .build();

        // then
        assertThat(largeValueStats.getAvgScore()).isEqualTo(999999.999999);

        // given
        SentimentStats negativeValueStats = SentimentStats.builder()
                .count(1)
                .avgScore(-0.75)
                .build();

        // then
        assertThat(negativeValueStats.getAvgScore()).isEqualTo(-0.75);
    }

    @Test
    void builder_WithZeroValues_ShouldCreateValidStats() {
        // when
        SentimentStats stats = SentimentStats.builder()
                .count(0)
                .avgScore(0.0)
                .build();

        // then
        assertThat(stats.getCount()).isZero();
        assertThat(stats.getAvgScore()).isZero();
    }

    @Test
    void setters_WithMaxValues_ShouldWork() {
        // given
        SentimentStats stats = SentimentStats.builder()
                .count(Integer.MAX_VALUE)
                .avgScore(Double.MAX_VALUE)
                .build();

        // then
        assertThat(stats.getCount()).isEqualTo(Integer.MAX_VALUE);
        assertThat(stats.getAvgScore()).isEqualTo(Double.MAX_VALUE);
    }

    @Test
    void setters_WithMinValues_ShouldWork() {
        // given
        SentimentStats stats = SentimentStats.builder()
                .count(Integer.MIN_VALUE)
                .avgScore(Double.MIN_VALUE)
                .build();

        // then
        assertThat(stats.getCount()).isEqualTo(Integer.MIN_VALUE);
        assertThat(stats.getAvgScore()).isEqualTo(Double.MIN_VALUE);
    }
}