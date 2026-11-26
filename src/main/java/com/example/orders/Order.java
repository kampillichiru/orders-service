package com.example.orders;

public record Order(long id, String customerId, long amountCents, String currency, String status) {
}
