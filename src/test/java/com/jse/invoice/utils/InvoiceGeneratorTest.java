package com.jse.invoice.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.dto.TransactionDto;
import com.jse.invoice.entity.Invoice;
import com.jse.invoice.entity.Transaction;

@ExtendWith(MockitoExtension.class)
public class InvoiceGeneratorTest {

    @Mock
    private InvoiceValidator invoiceValidator;

    @InjectMocks
    private InvoiceGenerator invoiceGenerator;

    @Test
    void shouldGenerateValidInvoice() {
        TransactionDto transactionDto = new TransactionDto(
            LocalDateTime.of(2026, 1, 10, 9, 0),
            LocalDateTime.of(2026, 1, 5, 9, 0),
            "00001",
            LocalDateTime.of(2026, 1, 1, 0, 0),
            LocalDateTime.of(2026, 1, 31, 0, 0),
            new BigDecimal("100.00"),
            new BigDecimal("10.00"));

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactionDtoList.add(transactionDto);

        InvoiceDto invoiceDto = new InvoiceDto(
            "00001",
            new BigDecimal("110.00"),
            new BigDecimal("10.00"),
            new BigDecimal("100.00"),
            LocalDateTime.of(2026, 2, 1, 9, 0),
            LocalDateTime.of(2026, 3, 1, 9, 0),
            Long.valueOf(1),
            transactionDtoList);

        when(invoiceValidator.verifyInvoice(invoiceDto)).thenReturn(Optional.empty());

        Invoice invoice = invoiceGenerator.generateInvoice(invoiceDto);

        verify(invoiceValidator, times(1)).verifyInvoice(invoiceDto);

        assertTrue(invoice.isValid());
        assertNull(invoice.getReason());
        assertEquals("00001", invoice.getInvoiceNumber());
        assertEquals(new BigDecimal("100.00"), invoice.getNetAmount());
        assertEquals(new BigDecimal("10.00"), invoice.getGstAmount());
        assertEquals(0, new BigDecimal("110.00").compareTo(invoice.getGrossAmount()));
        assertEquals(1L, invoice.getTotalNoTransactions());
    }

    @Test
    void shouldGenerateInvalidInvoiceWithReason() {
        InvoiceDto invoiceDto = new InvoiceDto(
            "00002",
            new BigDecimal("110.00"),
            new BigDecimal("10.00"),
            new BigDecimal("100.00"),
            LocalDateTime.of(2026, 2, 1, 9, 0),
            LocalDateTime.of(2026, 3, 1, 9, 0),
            Long.valueOf(0),
            new ArrayList<>());

        when(invoiceValidator.verifyInvoice(invoiceDto)).thenReturn(Optional.of("Net ammount does not match"));

        Invoice invoice = invoiceGenerator.generateInvoice(invoiceDto);

        verify(invoiceValidator, times(1)).verifyInvoice(invoiceDto);

        assertFalse(invoice.isValid());
        assertEquals("Net ammount does not match", invoice.getReason());
    }

    @Test
    void shouldMapTransactionsToInvoice() {
        TransactionDto firstTransactionDto = new TransactionDto(
            LocalDateTime.of(2026, 1, 10, 9, 0),
            LocalDateTime.of(2026, 1, 5, 9, 0),
            "00003",
            LocalDateTime.of(2026, 1, 1, 0, 0),
            LocalDateTime.of(2026, 1, 31, 0, 0),
            new BigDecimal("100.00"),
            new BigDecimal("10.00"));

        TransactionDto secondTransactionDto = new TransactionDto(
            LocalDateTime.of(2026, 2, 10, 9, 0),
            LocalDateTime.of(2026, 2, 5, 9, 0),
            "00003",
            LocalDateTime.of(2026, 2, 1, 0, 0),
            LocalDateTime.of(2026, 2, 28, 0, 0),
            new BigDecimal("50.00"),
            new BigDecimal("5.00"));

        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactionDtoList.add(firstTransactionDto);
        transactionDtoList.add(secondTransactionDto);

        InvoiceDto invoiceDto = new InvoiceDto(
            "00003",
            new BigDecimal("165.00"),
            new BigDecimal("15.00"),
            new BigDecimal("150.00"),
            LocalDateTime.of(2026, 3, 1, 9, 0),
            LocalDateTime.of(2026, 4, 1, 9, 0),
            Long.valueOf(2),
            transactionDtoList);

        when(invoiceValidator.verifyInvoice(invoiceDto)).thenReturn(Optional.empty());

        Invoice invoice = invoiceGenerator.generateInvoice(invoiceDto);

        assertEquals(2, invoice.getTransactions().size());

        Transaction firstTransaction = invoice.getTransactions().get(0);
        assertEquals(firstTransactionDto.dateReceived(), firstTransaction.getDateReceived());
        assertEquals(firstTransactionDto.transactionDate(), firstTransaction.getTransactionDate());
        assertEquals(firstTransactionDto.billingPeriodStart(), firstTransaction.getBillingPeriodStart());
        assertEquals(firstTransactionDto.billingPeriodEnd(), firstTransaction.getBillingPeriodEnd());
        assertEquals(firstTransactionDto.netTransactionAmount(), firstTransaction.getNetTransactionAmount());
        assertEquals(firstTransactionDto.gstAmount(), firstTransaction.getGstAmount());
        assertSame(invoice, firstTransaction.getInvoice());

        Transaction secondTransaction = invoice.getTransactions().get(1);
        assertEquals(secondTransactionDto.dateReceived(), secondTransaction.getDateReceived());
        assertEquals(secondTransactionDto.transactionDate(), secondTransaction.getTransactionDate());
        assertEquals(secondTransactionDto.billingPeriodStart(), secondTransaction.getBillingPeriodStart());
        assertEquals(secondTransactionDto.billingPeriodEnd(), secondTransaction.getBillingPeriodEnd());
        assertEquals(secondTransactionDto.netTransactionAmount(), secondTransaction.getNetTransactionAmount());
        assertEquals(secondTransactionDto.gstAmount(), secondTransaction.getGstAmount());
        assertSame(invoice, secondTransaction.getInvoice());
    }

    @Test
    void shouldGenerateInvoiceWithNoTransactions() {
        InvoiceDto invoiceDto = new InvoiceDto(
            "00004",
            new BigDecimal("0.00"),
            new BigDecimal("0.00"),
            new BigDecimal("0.00"),
            LocalDateTime.of(2026, 2, 1, 9, 0),
            LocalDateTime.of(2026, 3, 1, 9, 0),
            Long.valueOf(0),
            new ArrayList<>());

        when(invoiceValidator.verifyInvoice(invoiceDto)).thenReturn(Optional.empty());

        Invoice invoice = invoiceGenerator.generateInvoice(invoiceDto);

        assertNotNull(invoice);
        assertEquals("00004", invoice.getInvoiceNumber());
        assertTrue(invoice.getTransactions().isEmpty());
    }

    @Test
    void shouldCalculateGrossAmountFromGstAndNet() {
        InvoiceDto invoiceDto = new InvoiceDto(
            "00005",
            new BigDecimal("999.00"),
            new BigDecimal("20.00"),
            new BigDecimal("200.00"),
            LocalDateTime.of(2026, 2, 1, 9, 0),
            LocalDateTime.of(2026, 3, 1, 9, 0),
            Long.valueOf(0),
            new ArrayList<>());

        when(invoiceValidator.verifyInvoice(invoiceDto)).thenReturn(Optional.of("Invoice Gross ammount does not match"));

        Invoice invoice = invoiceGenerator.generateInvoice(invoiceDto);

        assertEquals(0, new BigDecimal("220.00").compareTo(invoice.getGrossAmount()));
    }
}
