package com.jse.invoice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.hamcrest.Matchers.hasSize;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.entity.Invoice;
import com.jse.invoice.entity.Transaction;
import com.jse.invoice.service.InvoiceService;

import tools.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

@WebMvcTest(InvoiceController.class)
public class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InvoiceService invoiceService;

    @Test
    void shouldReturnInvoicesList() throws Exception {
        // Given
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber("A000001");
        invoice.setGrossAmount(new BigDecimal(100.00));
        invoice.setGstAmount(new BigDecimal(10.00));
        invoice.setNetAmount(new BigDecimal(90.00));
        invoice.setReceiptDate(LocalDateTime.now());
        invoice.setPaymentDueDate(LocalDateTime.now());
        invoice.setTotalNoTransactions(4);
        invoice.setValid(false);
        invoice.setReason("Invalid");

        Transaction transaction = new Transaction();
        transaction.setId(2L);
        transaction.setDateReceived(LocalDateTime.now());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setBillingPeriodStart(LocalDateTime.now());
        transaction.setBillingPeriodEnd(LocalDateTime.now());
        transaction.setNetTransactionAmount(new BigDecimal(90));
        transaction.setGstAmount(new BigDecimal(10));

        invoice.getTransactions().add(transaction);

        List<Invoice> invoiceList = new ArrayList<>();
        invoiceList.add(invoice);

        when(invoiceService.getAllInvoices()).thenReturn(invoiceList);

        // When & Then
        mockMvc.perform(get("/invoice")
                .accept(MediaType.APPLICATION_JSON))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].transactions.[0].id").value(2L));
    }

    @Test
    void shouldReturnInvoice() throws Exception {
        // Given
        Invoice invoice = new Invoice();
        invoice.setId(1L);
        invoice.setInvoiceNumber("A000001");
        invoice.setGrossAmount(new BigDecimal(100.00));
        invoice.setGstAmount(new BigDecimal(10.00));
        invoice.setNetAmount(new BigDecimal(90.00));
        invoice.setReceiptDate(LocalDateTime.now());
        invoice.setPaymentDueDate(LocalDateTime.now());
        invoice.setTotalNoTransactions(4);
        invoice.setValid(false);
        invoice.setReason("Invalid");

        Transaction transaction = new Transaction();
        transaction.setId(2L);
        transaction.setDateReceived(LocalDateTime.now());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setBillingPeriodStart(LocalDateTime.now());
        transaction.setBillingPeriodEnd(LocalDateTime.now());
        transaction.setNetTransactionAmount(new BigDecimal(90));
        transaction.setGstAmount(new BigDecimal(10));

        invoice.getTransactions().add(transaction);

        when(invoiceService.getInvoice(1L)).thenReturn(invoice);

        // When & Then
        mockMvc.perform(get("/invoice/1")
                .accept(MediaType.APPLICATION_JSON))
                // .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.transactions.[0].id").value(2L));
    }

    @Test
    void shouldReturnInvoiceStatus() throws Exception {
        // Given
        Invoice invoice = new Invoice();
        invoice.setValid(false);
        invoice.setReason("Invalid");

        when(invoiceService.getInvoiceStatus(1L)).thenReturn("Invalid");

        // When & Then
        mockMvc.perform(get("/invoice/1/status")
                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("Invalid"));
    }

    @Test
    void shouldCreateInvoice() throws Exception {
        Invoice invoice = new Invoice();
        InvoiceDto invoiceDto = new InvoiceDto(null, null, null, null, null, null, null, null);

        when(invoiceService.saveInvoice(invoiceDto)).thenReturn(invoice);
        
        // Mockito.when(userService.createUser(any(UserRequest.class))).thenReturn(mockResponse);
        mockMvc.perform(post("/invoice")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invoiceDto)))
            .andDo(print())
            .andExpect(status().isCreated());
    }
}
