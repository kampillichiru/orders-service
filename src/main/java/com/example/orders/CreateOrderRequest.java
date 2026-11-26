package com.example.orders;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
        @NotBlank String customerId,
        @Positive long amountCents,
        @NotBlank @Pattern(regexp = "[A-Z]{3}") String currency) {
}
