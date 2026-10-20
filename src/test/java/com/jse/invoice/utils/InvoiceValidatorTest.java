package com.jse.invoice.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.dto.TransactionDto;

@ExtendWith(MockitoExtension.class)
public class InvoiceValidatorTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2026, 1, 15, 10, 0);

    @Mock 
    private InvoiceValidatorConditions invoiceValidatorConditions;

    @InjectMocks
    private InvoiceValidator invoiceValidator;

    private InvoiceDto invoiceDto;

    @BeforeEach
    void setUp() {
        TransactionDto transactionDto = new TransactionDto(
            FIXED_DATE,
            FIXED_DATE,
            "00001",
            FIXED_DATE,
            FIXED_DATE.plusMonths(1),
            new BigDecimal("10.00"),
            new BigDecimal("1.00"));

        invoiceDto = new InvoiceDto(
            "00001",
            new BigDecimal("11.00"),
            new BigDecimal("1.00"),
            new BigDecimal("10.00"),
            FIXED_DATE,
            FIXED_DATE.plusDays(30),
            Long.valueOf(1),
            List.of(transactionDto));
    }

    @Test
    void shouldReturnEmptyWhenAllConditionsPass() {
        Optional<String> validationResult = invoiceValidator.verifyInvoice(invoiceDto);

        assertThat(validationResult, is(Optional.empty()));
    }

    @Test
    void shouldVerifyNoOfTransactions() {
        when(invoiceValidatorConditions.verifyInvoiceTransactionListSize(invoiceDto)).thenReturn(true);

        Optional<String> validationResult = invoiceValidator.verifyInvoice(invoiceDto);

        assertThat(validationResult, is(Optional.of("List of transactions does not match the number of transactions in the invoice")));
    }

    @Test
    void shouldReturnReasonWhenNetAmountDoesNotMatch() {
        when(invoiceValidatorConditions.verifyInvoiceNetAmount(invoiceDto)).thenReturn(true);

        Optional<String> validationResult = invoiceValidator.verifyInvoice(invoiceDto);

        assertThat(validationResult, is(Optional.of("Net amount does not match")));
    }

    @Test
    void shouldReturnReasonWhenGstAmountDoesNotMatch() {
        when(invoiceValidatorConditions.verifyInvoiceGstAmount(invoiceDto)).thenReturn(true);

        Optional<String> validationResult = invoiceValidator.verifyInvoice(invoiceDto);

        assertThat(validationResult, is(Optional.of("GST amount does not match")));
    }

    @Test
    void shouldReturnReasonWhenGrossAmountDoesNotMatch() {
        when(invoiceValidatorConditions.verifyTotalAmountInvoice(invoiceDto)).thenReturn(true);

        Optional<String> validationResult = invoiceValidator.verifyInvoice(invoiceDto);

        assertThat(validationResult, is(Optional.of("Invoice Gross amount does not match")));
    }

    @Test
    void shouldConcatenateReasonsWhenMultipleConditionsFail() {
        when(invoiceValidatorConditions.verifyInvoiceNetAmount(invoiceDto)).thenReturn(true);
        when(invoiceValidatorConditions.verifyInvoiceGstAmount(invoiceDto)).thenReturn(true);

        Optional<String> validationResult = invoiceValidator.verifyInvoice(invoiceDto);

        assertThat(validationResult, is(Optional.of("Net amount does not match; GST amount does not match")));
    }
}
