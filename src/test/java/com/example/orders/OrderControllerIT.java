package com.example.orders;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration test: the full application context, HTTP through MockMvc.
 * The repository is shared between tests, so assertions use the ids they create.
 */
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIT {

    private static final String JSON = MediaType.APPLICATION_JSON_VALUE;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void createReturnsCreatedOrder() throws Exception {
        mvc.perform(post("/api/orders").contentType(JSON)
                        .content("{\"customerId\":\"c1\",\"amountCents\":1000,\"currency\":\"USD\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value("c1"))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void createRejectsNonPositiveAmount() throws Exception {
        mvc.perform(post("/api/orders").contentType(JSON)
                        .content("{\"customerId\":\"c1\",\"amountCents\":0,\"currency\":\"USD\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReturnsTheCreatedOrder() throws Exception {
        long id = createOrder("c2", 500, "EUR");

        mvc.perform(get("/api/orders/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void getUnknownOrderIsNotFound() throws Exception {
        mvc.perform(get("/api/orders/999999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listContainsTheCreatedOrder() throws Exception {
        long id = createOrder("c3", 700, "GBP");

        mvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")]").exists());
    }

    @Test
    void totalsAreReturnedPerCurrency() throws Exception {
        createOrder("c4", 300, "JPY");

        mvc.perform(get("/api/orders/totals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.JPY").exists());
    }

    @Test
    void reportCsvStartsWithHeader() throws Exception {
        mvc.perform(get("/api/orders/report.csv"))
                .andExpect(status().isOk())
                .andExpect(content().string(startsWith("id,customerId")));
    }

    @Test
    void healthIsUp() throws Exception {
        mvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    private long createOrder(String customer, long cents, String currency) throws Exception {
        String body = String.format(
                "{\"customerId\":\"%s\",\"amountCents\":%d,\"currency\":\"%s\"}", customer, cents, currency);
        MvcResult result = mvc.perform(post("/api/orders").contentType(JSON).content(body))
                .andExpect(status().isCreated())
                .andReturn();
        return mapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }
}
