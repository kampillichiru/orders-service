package com.example.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/** Unit test: the service with a real in-memory repository, no Spring context. */
class OrderServiceTest {

    private final OrderService service = new OrderService(new OrderRepository());

    @Test
    void createAssignsIncreasingIdsAndStatusNew() {
        Order first = service.create(new CreateOrderRequest("c1", 100, "EUR"));
        Order second = service.create(new CreateOrderRequest("c2", 250, "EUR"));

        assertEquals(1, first.id());
        assertEquals(2, second.id());
        assertEquals("NEW", first.status());
    }

    @Test
    void findReturnsEmptyForUnknownId() {
        assertTrue(service.find(99).isEmpty());
    }

    @Test
    void listIsSortedById() {
        service.create(new CreateOrderRequest("c1", 10, "USD"));
        service.create(new CreateOrderRequest("c2", 20, "USD"));

        List<Long> ids = service.list().stream().map(Order::id).toList();
        assertEquals(List.of(1L, 2L), ids);
    }

    @Test
    void totalsAreGroupedByCurrency() {
        service.create(new CreateOrderRequest("c1", 100, "USD"));
        service.create(new CreateOrderRequest("c2", 50, "USD"));
        service.create(new CreateOrderRequest("c3", 30, "EUR"));

        Map<String, Long> totals = service.totalsByCurrency();
        assertEquals(150L, totals.get("USD"));
        assertEquals(30L, totals.get("EUR"));
    }
}
