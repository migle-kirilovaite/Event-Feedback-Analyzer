package com.example.eventsync.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateEventRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void builderPattern_ShouldCreateRequestWithAllFields() {
        // given
        String title = "Test Event";
        String description = "Test Description";

        // when
        CreateEventRequest request = CreateEventRequest.builder()
                .title(title)
                .description(description)
                .build();

        // then
        assertThat(request.getTitle()).isEqualTo(title);
        assertThat(request.getDescription()).isEqualTo(description);
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // given
        CreateEventRequest request = new CreateEventRequest();

        // when
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");

        // then
        assertThat(request.getTitle()).isEqualTo("Updated Title");
        assertThat(request.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    void allArgsConstructor_ShouldCreateRequestWithAllFields() {
        // given
        String title = "Test Event";
        String description = "Test Description";

        // when
        CreateEventRequest request = new CreateEventRequest(title, description);

        // then
        assertThat(request.getTitle()).isEqualTo(title);
        assertThat(request.getDescription()).isEqualTo(description);
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyRequest() {
        // when
        CreateEventRequest request = new CreateEventRequest();

        // then
        assertThat(request.getTitle()).isNull();
        assertThat(request.getDescription()).isNull();
    }

    @Test
    void validate_WhenTitleIsBlank_ShouldHaveViolation() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .title("")
                .description("Valid description")
                .build();

        // when
        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateEventRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Title must not be blank");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("title");
    }

    @Test
    void validate_WhenTitleIsNull_ShouldHaveViolation() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .description("Valid description")
                .build();

        // when
        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateEventRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Title must not be blank");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("title");
    }

    @Test
    void validate_WhenTitleExceeds64Characters_ShouldHaveViolation() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .title("a".repeat(65))
                .description("Valid description")
                .build();

        // when
        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateEventRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Title must not exceed 64 characters");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("title");
    }

    @Test
    void validate_WhenDescriptionExceeds256Characters_ShouldHaveViolation() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .title("Valid Title")
                .description("a".repeat(257))
                .build();

        // when
        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
        ConstraintViolation<CreateEventRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Description must not exceed 256 characters");
        assertThat(violation.getPropertyPath().toString()).isEqualTo("description");
    }

    @Test
    void validate_WhenAllFieldsAreValid_ShouldHaveNoViolations() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .title("Valid Title")
                .description("Valid Description")
                .build();

        // when
        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    void validate_WhenDescriptionIsNull_ShouldBeValid() {
        // given
        CreateEventRequest request = CreateEventRequest.builder()
                .title("Valid Title")
                .build();

        // when
        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }
}