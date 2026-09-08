package com.jse.invoice.utils;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.dto.TransactionDto;

@Component
public class InvoiceValidator {
    public Optional<String> verifyInvoice(InvoiceDto invoiceDto) {
        StringBuilder reasons = new StringBuilder();

        if (invoiceDto.totalNoTransactions() != invoiceDto.transactionDtoList().size()){
            reasons.append("- List of transactions does not match the number of transactions in the invoice");
        }

        // Checking the Net ammount
        BigDecimal netSum = invoiceDto.transactionDtoList().stream()
                              .map(TransactionDto::netTransactionAmount)
                              .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (invoiceDto.netAmount().compareTo(netSum) != 0) {
            reasons.append("- Net ammount does not match");
        }

        // Checking the GST ammount
        BigDecimal gstSum = invoiceDto.transactionDtoList().stream()
                              .map(TransactionDto::gstAmount)
                              .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (invoiceDto.gstAmount().compareTo(gstSum) != 0) {
            reasons.append("- GST ammount does not match");
        }

        if (invoiceDto.gstAmount().add(invoiceDto.netAmount()).compareTo(invoiceDto.grossAmount()) != 0) {
            reasons.append("- Invoice Gross ammount does not match");
        }

        if (reasons.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(reasons.toString());
        }
    }
}