package com.jse.invoice.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
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

        assertThat(invoice.isValid(), is(true));
        assertThat(invoice.getReason(), is(nullValue()));
        assertThat(invoice.getInvoiceNumber(), is("00001"));
        assertThat(invoice.getNetAmount(), comparesEqualTo(new BigDecimal("100.00")));
        assertThat(invoice.getGstAmount(), comparesEqualTo(new BigDecimal("10.00")));
        assertThat(invoice.getGrossAmount(), comparesEqualTo(new BigDecimal("110.00")));
        assertThat(invoice.getTotalNoTransactions(), is(1L));
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

        when(invoiceValidator.verifyInvoice(invoiceDto)).thenReturn(Optional.of("Net amount does not match"));

        Invoice invoice = invoiceGenerator.generateInvoice(invoiceDto);

        verify(invoiceValidator, times(1)).verifyInvoice(invoiceDto);

        assertThat(invoice.isValid(), is(false));
        assertThat(invoice.getReason(), is("Net amount does not match"));
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

        assertThat(invoice.getTransactions(), hasSize(2));

        Transaction firstTransaction = invoice.getTransactions().get(0);
        assertThat(firstTransaction.getDateReceived(), is(firstTransactionDto.dateReceived()));
        assertThat(firstTransaction.getTransactionDate(), is(firstTransactionDto.transactionDate()));
        assertThat(firstTransaction.getBillingPeriodStart(), is(firstTransactionDto.billingPeriodStart()));
        assertThat(firstTransaction.getBillingPeriodEnd(), is(firstTransactionDto.billingPeriodEnd()));
        assertThat(firstTransaction.getNetTransactionAmount(), comparesEqualTo(firstTransactionDto.netTransactionAmount()));
        assertThat(firstTransaction.getGstAmount(), comparesEqualTo(firstTransactionDto.gstAmount()));
        assertThat(firstTransaction.getInvoice(), is(sameInstance(invoice)));

        Transaction secondTransaction = invoice.getTransactions().get(1);
        assertThat(secondTransaction.getDateReceived(), is(secondTransactionDto.dateReceived()));
        assertThat(secondTransaction.getTransactionDate(), is(secondTransactionDto.transactionDate()));
        assertThat(secondTransaction.getBillingPeriodStart(), is(secondTransactionDto.billingPeriodStart()));
        assertThat(secondTransaction.getBillingPeriodEnd(), is(secondTransactionDto.billingPeriodEnd()));
        assertThat(secondTransaction.getNetTransactionAmount(), comparesEqualTo(secondTransactionDto.netTransactionAmount()));
        assertThat(secondTransaction.getGstAmount(), comparesEqualTo(secondTransactionDto.gstAmount()));
        assertThat(secondTransaction.getInvoice(), is(sameInstance(invoice)));
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

        assertThat(invoice, is(notNullValue()));
        assertThat(invoice.getInvoiceNumber(), is("00004"));
        assertThat(invoice.getTransactions(), is(empty()));
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

        when(invoiceValidator.verifyInvoice(invoiceDto)).thenReturn(Optional.of("Invoice Gross amount does not match"));

        Invoice invoice = invoiceGenerator.generateInvoice(invoiceDto);

        assertThat(invoice.getGrossAmount(), comparesEqualTo(new BigDecimal("220.00")));
    }
}
