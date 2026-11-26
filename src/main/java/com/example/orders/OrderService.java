package com.example.orders;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public Order create(CreateOrderRequest request) {
        return repository.save(request.customerId(), request.amountCents(), request.currency());
    }

    public Optional<Order> find(long id) {
        return repository.findById(id);
    }

    public List<Order> list() {
        return repository.findAll().stream()
                .sorted(Comparator.comparingLong(Order::id))
                .toList();
    }

    /** Total amount in cents for each currency. */
    public Map<String, Long> totalsByCurrency() {
        Map<String, Long> totals = new TreeMap<>();
        for (Order order : repository.findAll()) {
            totals.merge(order.currency(), order.amountCents(), Long::sum);
        }
        return totals;
    }

    /**
     * Deliberately has no test. It shows up as untraced code in the impact report.
     */
    public Optional<Order> cancel(long id) {
        return repository.updateStatus(id, "CANCELLED");
    }
}
