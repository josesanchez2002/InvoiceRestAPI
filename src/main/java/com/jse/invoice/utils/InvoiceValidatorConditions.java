package com.jse.invoice.utils;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.dto.TransactionDto;

@Component 
public class InvoiceValidatorConditions {

    public boolean verifyInvoiceTransactionListSize(InvoiceDto invoiceDto) {
        return invoiceDto.totalNoTransactions() != invoiceDto.transactionDtoList().size();
    }

    public boolean verifyInvoiceNetAmount(InvoiceDto invoiceDto) {
        // Checking the Net ammount
        BigDecimal netSum = invoiceDto.transactionDtoList().stream()
                              .map(TransactionDto::netTransactionAmount)
                              .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return (invoiceDto.netAmount().compareTo(netSum) != 0);
    }

    public boolean verifyInvoiceGstAmount(InvoiceDto invoiceDto) {
        // Checking the GST ammount
        BigDecimal gstSum = invoiceDto.transactionDtoList().stream()
                              .map(TransactionDto::gstAmount)
                              .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return (invoiceDto.gstAmount().compareTo(gstSum) != 0);
    }

    public boolean verifyTotalAmountInvoice(InvoiceDto invoiceDto) {
        return (invoiceDto.gstAmount().add(invoiceDto.netAmount()).compareTo(invoiceDto.grossAmount()) != 0);
    }
}
