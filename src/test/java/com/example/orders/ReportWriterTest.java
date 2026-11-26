package com.example.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class ReportWriterTest {

    private final ReportWriter writer = new ReportWriter();

    @Test
    void ordersCsvHasHeaderAndOneLinePerOrder() {
        String csv = writer.ordersCsv(List.of(new Order(1, "c1", 100, "USD", "NEW")));

        assertEquals("id,customerId,amountCents,currency,status\n1,c1,100,USD,NEW\n", csv);
    }
}
