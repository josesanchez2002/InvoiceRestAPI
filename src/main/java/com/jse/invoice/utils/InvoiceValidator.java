package com.jse.invoice.utils;

import org.springframework.stereotype.Component;

import com.jse.invoice.dto.InvoiceDto;

@Component
public class InvoiceValidator {
    public String verifyInvoice(InvoiceDto invoiceDto) {

        StringBuilder reasons = new StringBuilder();

        if (invoiceDto.totalNoTransactions() != invoiceDto.transactionDtoList().size()){
            reasons.append("List of transactions does not match the number of transactions in the invopice");
        }

        if (reasons.isEmpty()){
            return null;
        } else {
            return reasons.toString();
        }
    }
    
}
