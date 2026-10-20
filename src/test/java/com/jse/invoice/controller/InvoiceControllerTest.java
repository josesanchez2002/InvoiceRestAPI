package com.jse.invoice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.entity.Invoice;
import com.jse.invoice.entity.Transaction;
import com.jse.invoice.exception.InvoiceNotFoundException;
import com.jse.invoice.service.InvoiceService;

import tools.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2026, 1, 15, 10, 0);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InvoiceService invoiceService;

    @Test
    void shouldReturnInvoicesList() throws Exception {
        // Given
        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(invoiceWithTransaction());

        when(invoiceService.getAllInvoices()).thenReturn(invoiceList);

        // When & Then
        mockMvc.perform(get("/invoice")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].transactions.[0].id").value(2L));
    }

    @Test
    void shouldReturnInvoice() throws Exception {
        // Given
        when(invoiceService.getInvoice(1L)).thenReturn(invoiceWithTransaction());

        // When & Then
        mockMvc.perform(get("/invoice/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.transactions.[0].id").value(2L));
    }

    @Test
    void shouldReturnNotFoundWhenInvoiceDoesNotExist() throws Exception {
        // Given
        when(invoiceService.getInvoice(99L)).thenThrow(new InvoiceNotFoundException(99L));

        // When & Then
        mockMvc.perform(get("/invoice/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnInvoiceStatus() throws Exception {
        // Given
        when(invoiceService.getInvoiceStatus(1L)).thenReturn("Invalid");

        // When & Then
        mockMvc.perform(get("/invoice/1/status")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid"));
    }

    @Test
    void shouldReturnNotFoundWhenInvoiceStatusDoesNotExist() throws Exception {
        // Given
        when(invoiceService.getInvoiceStatus(99L)).thenThrow(new InvoiceNotFoundException(99L));

        // When & Then
        mockMvc.perform(get("/invoice/99/status")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateInvoice() throws Exception {
        // Given
        Invoice invoice = new Invoice();
        InvoiceDto invoiceDto = new InvoiceDto(
            "A000001",
            new BigDecimal("110.00"),
            new BigDecimal("10.00"),
            new BigDecimal("100.00"),
            FIXED_DATE,
            FIXED_DATE.plusDays(30),
            Long.valueOf(0),
            List.of());

        when(invoiceService.saveInvoice(invoiceDto)).thenReturn(invoice);

        // When & Then
        mockMvc.perform(post("/invoice")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invoiceDto)))
            .andExpect(status().isCreated())
            .andExpect(content().string(startsWith("Invoice A000001 created successfully")));
    }

    private Invoice invoiceWithTransaction() {
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber("A000001");
        invoice.setGrossAmount(new BigDecimal("100.00"));
        invoice.setGstAmount(new BigDecimal("10.00"));
        invoice.setNetAmount(new BigDecimal("90.00"));
        invoice.setReceiptDate(FIXED_DATE);
        invoice.setPaymentDueDate(FIXED_DATE.plusDays(30));
        invoice.setTotalNoTransactions(4);
        invoice.setValid(false);
        invoice.setReason("Invalid");

        Transaction transaction = new Transaction();
        transaction.setId(2L);
        transaction.setDateReceived(FIXED_DATE);
        transaction.setTransactionDate(FIXED_DATE);
        transaction.setBillingPeriodStart(FIXED_DATE);
        transaction.setBillingPeriodEnd(FIXED_DATE.plusMonths(1));
        transaction.setNetTransactionAmount(new BigDecimal("90.00"));
        transaction.setGstAmount(new BigDecimal("10.00"));

        invoice.getTransactions().add(transaction);

        return invoice;
    }
}
