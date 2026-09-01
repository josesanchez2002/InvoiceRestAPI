package com.jse.invoice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

import java.time.LocalDateTime;

public record TransactionDto(
        @NotNull(message = "Date Received is required")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        LocalDateTime dateReceived,

        @NotNull(message = "Transaction Date is required")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        LocalDateTime transactionDate,
        
        @NotNull(message = "Invoice Number is required")
        String invoiceNumber,

        @NotNull(message = "Start Billing Period is required")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        LocalDateTime billingPeriodStart,

        @NotNull(message = "End Billing Period is required")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        LocalDateTime billingPeriodEnd,

        @NotNull(message = "Net transaction amount is required")
        @DecimalMin(value = "0.01", message = "Net transaction amount is required")
        BigDecimal netTransactionAmount,
        
        @NotNull(message = "GST transaction amount is required")
        @DecimalMin(value = "0.01", message = "GST transaction amount is required")
        BigDecimal gstAmount
) {
}
