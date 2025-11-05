package com.example.eventsync.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateFeedbackRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void builderPattern_ShouldCreateRequestWithText() {
        // given
        String text = "Great event, really enjoyed it!";

        // when
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text(text)
                .build();

        // then
        assertThat(request.getText()).isEqualTo(text);
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // given
        CreateFeedbackRequest request = new CreateFeedbackRequest();
        String text = "This was an amazing experience!";

        // when
        request.setText(text);

        // then
        assertThat(request.getText()).isEqualTo(text);
    }

    @Test
    void allArgsConstructor_ShouldCreateRequestWithText() {
        // given
        String text = "Wonderful organization and content!";

        // when
        CreateFeedbackRequest request = new CreateFeedbackRequest(text);

        // then
        assertThat(request.getText()).isEqualTo(text);
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyRequest() {
        // when
        CreateFeedbackRequest request = new CreateFeedbackRequest();

        // then
        assertThat(request.getText()).isNull();
    }

    @Test
    void validate_WhenTextIsBlank_ShouldHaveViolation() {
        // given
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text("")
                .build();

        // when
        Set<ConstraintViolation<CreateFeedbackRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateFeedbackRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Feedback text must not be blank");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("text");
    }

    @Test
    void validate_WhenTextIsNull_ShouldHaveViolation() {
        // given
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text(null)
                .build();

        // when
        Set<ConstraintViolation<CreateFeedbackRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateFeedbackRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Feedback text must not be blank");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("text");
    }

    @Test
    void validate_WhenTextExceeds1000Characters_ShouldHaveViolation() {
        // given
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text("a".repeat(1001))
                .build();

        // when
        Set<ConstraintViolation<CreateFeedbackRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateFeedbackRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Feedback text must not exceed 2000 characters");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("text");
    }

    @Test
    void validate_WhenTextIsExactly1000Characters_ShouldBeValid() {
        // given
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text("a".repeat(1000))
                .build();

        // when
        Set<ConstraintViolation<CreateFeedbackRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void validate_WhenTextIsValid_ShouldHaveNoViolations() {
        // given
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .text("Great event! Looking forward to the next one.")
                .build();

        // when
        Set<ConstraintViolation<CreateFeedbackRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }
}