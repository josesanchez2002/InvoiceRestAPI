package com.jse.invoice.utils;

import java.util.Optional;
import java.util.StringJoiner;

import org.springframework.stereotype.Component;

import com.jse.invoice.dto.InvoiceDto;

@Component
public class InvoiceValidator {

    private final InvoiceValidatorConditions invoiceValidatorConditions;

    public InvoiceValidator(InvoiceValidatorConditions invoiceValidatorConditions) {
        this.invoiceValidatorConditions = invoiceValidatorConditions;
    }

    public Optional<String> verifyInvoice(InvoiceDto invoiceDto) {
        StringJoiner reasons = new StringJoiner("; ");

        if (invoiceValidatorConditions.verifyInvoiceTransactionListSize(invoiceDto)){
            reasons.add("List of transactions does not match the number of transactions in the invoice");
        }

        if (invoiceValidatorConditions.verifyInvoiceNetAmount(invoiceDto)){
            reasons.add("Net amount does not match");
        }

        if (invoiceValidatorConditions.verifyInvoiceGstAmount(invoiceDto)){
            reasons.add("GST amount does not match");
        }

        if (invoiceValidatorConditions.verifyTotalAmountInvoice(invoiceDto)){
            reasons.add("Invoice Gross amount does not match");
        }

        if (reasons.length() == 0){
            return Optional.empty();
        } else {
            return Optional.of(reasons.toString());
        }
    }
}