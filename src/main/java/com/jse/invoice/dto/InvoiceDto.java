package com.jse.invoice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record InvoiceDto(
        @NotBlank(message = "Invoice number cannot be blank")
        String invoiceNumber,

        @NotNull(message = "Gross amount is required")
        @DecimalMin(value = "0.01", message = "Gross amount is required")
        BigDecimal grossAmount,

        @NotNull(message = "GST amount is required")
        @DecimalMin(value = "0.01", message = "GST amount is required")
        BigDecimal gstAmount,

        @NotNull(message = "Net amount is required")
        @DecimalMin(value = "0.01", message = "Net amount is required")
        BigDecimal netAmount,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
        LocalDateTime receiptDate,

        @NotNull(message = "Payment due date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
        LocalDateTime paymentDueDate,

        @NotNull(message = "Total number of transactions is required")
        @Min(value = 0, message = "Total number of transactions cannot be negative")
        Long totalNoTransactions,

        @NotNull(message = "Transaction data is required")
        List<TransactionDto> transactionDtoList
) {
}
