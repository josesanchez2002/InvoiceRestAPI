package com.jse.invoice.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.dto.TransactionDto;

@ExtendWith(MockitoExtension.class)
public class InvoiceValidatorConditionsTest {

    private static final LocalDateTime FIXED_DATE = LocalDateTime.of(2026, 1, 15, 10, 0);

    @InjectMocks
    private InvoiceValidatorConditions invoiceValidatorConditions;

    // verifyInvoiceTransactionListSize

    @Test
    void shouldReturnFalseWhenTransactionCountMatchesListSize() {
        List<TransactionDto> transactionDtoList = List.of(
            transaction("10.00", "1.00"),
            transaction("5.00", "0.50"));
        InvoiceDto invoiceDto = invoice("16.50", "1.50", "15.00", 2L, transactionDtoList);

        boolean result = invoiceValidatorConditions.verifyInvoiceTransactionListSize(invoiceDto);

        assertThat(result, is(false));
    }

    @Test
    void shouldReturnTrueWhenTransactionCountDoesNotMatchListSize() {
        List<TransactionDto> transactionDtoList = List.of(transaction("10.00", "1.00"));
        InvoiceDto invoiceDto = invoice("11.00", "1.00", "10.00", 4L, transactionDtoList);

        boolean result = invoiceValidatorConditions.verifyInvoiceTransactionListSize(invoiceDto);

        assertThat(result, is(true));
    }

    @Test
    void shouldReturnFalseWhenTransactionListIsEmptyAndCountIsZero() {
        InvoiceDto invoiceDto = invoice("11.00", "1.00", "10.00", 0L, List.of());

        boolean result = invoiceValidatorConditions.verifyInvoiceTransactionListSize(invoiceDto);

        assertThat(result, is(false));
    }

    // verifyInvoiceNetAmount

    @Test
    void shouldReturnFalseWhenNetAmountEqualsSumOfTransactions() {
        List<TransactionDto> transactionDtoList = List.of(
            transaction("10.00", "1.00"),
            transaction("5.50", "0.55"));
        InvoiceDto invoiceDto = invoice("17.05", "1.55", "15.50", 2L, transactionDtoList);

        boolean result = invoiceValidatorConditions.verifyInvoiceNetAmount(invoiceDto);

        assertThat(result, is(false));
    }

    @Test
    void shouldReturnTrueWhenNetAmountDiffersFromSumOfTransactions() {
        List<TransactionDto> transactionDtoList = List.of(
            transaction("10.00", "1.00"),
            transaction("5.50", "0.55"));
        InvoiceDto invoiceDto = invoice("21.55", "1.55", "20.00", 2L, transactionDtoList);

        boolean result = invoiceValidatorConditions.verifyInvoiceNetAmount(invoiceDto);

        assertThat(result, is(true));
    }

    @Test
    void shouldReturnFalseWhenNetAmountScaleDiffersButValueIsEqual() {
        List<TransactionDto> transactionDtoList = List.of(
            transaction("10.00", "1.00"),
            transaction("5.50", "0.55"));
        InvoiceDto invoiceDto = invoice("17.05", "1.55", "15.5", 2L, transactionDtoList);

        boolean result = invoiceValidatorConditions.verifyInvoiceNetAmount(invoiceDto);

        assertThat(result, is(false));
    }

    @Test
    void shouldReturnTrueWhenTransactionListIsEmptyAndNetAmountIsPositive() {
        InvoiceDto invoiceDto = invoice("11.00", "1.00", "10.00", 0L, List.of());

        boolean result = invoiceValidatorConditions.verifyInvoiceNetAmount(invoiceDto);

        assertThat(result, is(true));
    }

    // verifyInvoiceGstAmount

    @Test
    void shouldReturnFalseWhenGstAmountEqualsSumOfTransactions() {
        List<TransactionDto> transactionDtoList = List.of(
            transaction("10.00", "1.00"),
            transaction("5.50", "0.55"));
        InvoiceDto invoiceDto = invoice("17.05", "1.55", "15.50", 2L, transactionDtoList);

        boolean result = invoiceValidatorConditions.verifyInvoiceGstAmount(invoiceDto);

        assertThat(result, is(false));
    }

    @Test
    void shouldReturnTrueWhenGstAmountDiffersFromSumOfTransactions() {
        List<TransactionDto> transactionDtoList = List.of(
            transaction("10.00", "1.00"),
            transaction("5.50", "0.55"));
        InvoiceDto invoiceDto = invoice("17.50", "2.00", "15.50", 2L, transactionDtoList);

        boolean result = invoiceValidatorConditions.verifyInvoiceGstAmount(invoiceDto);

        assertThat(result, is(true));
    }

    @Test
    void shouldReturnFalseWhenGstAmountScaleDiffersButValueIsEqual() {
        List<TransactionDto> transactionDtoList = List.of(
            transaction("10.00", "1.00"),
            transaction("5.50", "0.50"));
        InvoiceDto invoiceDto = invoice("17.00", "1.5", "15.50", 2L, transactionDtoList);

        boolean result = invoiceValidatorConditions.verifyInvoiceGstAmount(invoiceDto);

        assertThat(result, is(false));
    }

    // verifyTotalAmountInvoice

    @Test
    void shouldReturnFalseWhenGrossEqualsNetPlusGst() {
        InvoiceDto invoiceDto = invoice("110.00", "10.00", "100.00", 0L, List.of());

        boolean result = invoiceValidatorConditions.verifyTotalAmountInvoice(invoiceDto);

        assertThat(result, is(false));
    }

    @Test
    void shouldReturnTrueWhenGrossDoesNotEqualNetPlusGst() {
        InvoiceDto invoiceDto = invoice("120.00", "10.00", "100.00", 0L, List.of());

        boolean result = invoiceValidatorConditions.verifyTotalAmountInvoice(invoiceDto);

        assertThat(result, is(true));
    }

    @Test
    void shouldReturnFalseWhenGrossScaleDiffersButValueIsEqual() {
        InvoiceDto invoiceDto = invoice("110", "10.00", "100.00", 0L, List.of());

        boolean result = invoiceValidatorConditions.verifyTotalAmountInvoice(invoiceDto);

        assertThat(result, is(false));
    }

    private TransactionDto transaction(String netAmount, String gstAmount) {
        return new TransactionDto(
            FIXED_DATE,
            FIXED_DATE,
            "00001",
            FIXED_DATE,
            FIXED_DATE.plusMonths(1),
            new BigDecimal(netAmount),
            new BigDecimal(gstAmount));
    }

    private InvoiceDto invoice(String grossAmount, String gstAmount, String netAmount,
                               Long totalNoTransactions, List<TransactionDto> transactionDtoList) {
        return new InvoiceDto(
            "00001",
            new BigDecimal(grossAmount),
            new BigDecimal(gstAmount),
            new BigDecimal(netAmount),
            FIXED_DATE,
            FIXED_DATE.plusDays(30),
            totalNoTransactions,
            transactionDtoList);
    }
}
