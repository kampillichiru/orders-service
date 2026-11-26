package com.example.orders;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReportWriter {

    /** Orders as CSV, with a header row. */
    public String ordersCsv(List<Order> orders) {
        StringBuilder csv = new StringBuilder("id,customerId,amountCents,currency,status\n");
        for (Order order : orders) {
            csv.append(order.id()).append(',')
               .append(order.customerId()).append(',')
               .append(order.amountCents()).append(',')
               .append(order.currency()).append(',')
               .append(order.status()).append('\n');
        }
        return csv.toString();
    }
}
