package com.jse.invoice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

import java.time.LocalDateTime;

public record TransactionDto(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
        LocalDateTime dateReceived,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
        LocalDateTime transactionDate,
        String invoiceNumber,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
        LocalDateTime billingPeriodStart,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
        LocalDateTime billingPeriodEnd,

        BigDecimal netTransactionAmount,
        BigDecimal gstAmount
) {
}
