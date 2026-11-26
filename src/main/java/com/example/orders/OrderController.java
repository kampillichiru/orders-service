package com.example.orders;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orders;
    private final ReportWriter reports;

    public OrderController(OrderService orders, ReportWriter reports) {
        this.orders = orders;
        this.reports = reports;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@Valid @RequestBody CreateOrderRequest request) {
        return orders.create(request);
    }

    @GetMapping
    public List<Order> list() {
        return orders.list();
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable long id) {
        return orders.find(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @GetMapping("/totals")
    public Map<String, Long> totals() {
        return orders.totalsByCurrency();
    }

    @GetMapping(value = "/report.csv", produces = "text/csv")
    public String reportCsv() {
        return reports.ordersCsv(orders.list());
    }

    @PostMapping("/{id}/cancel")
    public Order cancel(@PathVariable long id) {
        return orders.cancel(id).orElseThrow(() -> new OrderNotFoundException(id));
    }
}
