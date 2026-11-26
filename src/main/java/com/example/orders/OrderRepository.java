package com.example.orders;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

/** In-memory store for the POC. A real service would use a database. */
@Repository
public class OrderRepository {

    private final Map<Long, Order> orders = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public Order save(String customerId, long amountCents, String currency) {
        long id = sequence.getAndIncrement();
        Order order = new Order(id, customerId, amountCents, currency, "NEW");
        orders.put(id, order);
        return order;
    }

    public Optional<Order> findById(long id) {
        return Optional.ofNullable(orders.get(id));
    }

    public Collection<Order> findAll() {
        return orders.values();
    }

    public Optional<Order> updateStatus(long id, String status) {
        return findById(id).map(existing -> {
            Order updated = new Order(existing.id(), existing.customerId(), existing.amountCents(),
                    existing.currency(), status);
            orders.put(id, updated);
            return updated;
        });
    }
}
