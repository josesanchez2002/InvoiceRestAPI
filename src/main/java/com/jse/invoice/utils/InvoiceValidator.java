package com.jse.invoice.utils;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.jse.invoice.dto.InvoiceDto;

@Component
public class InvoiceValidator {

    private final InvoiceValidatorConditions invoiceValidatorConditions;

    public InvoiceValidator(InvoiceValidatorConditions invoiceValidatorConditions) {
        this.invoiceValidatorConditions = invoiceValidatorConditions;
    }

    public Optional<String> verifyInvoice(InvoiceDto invoiceDto) {
        StringBuilder reasons = new StringBuilder();

        if (invoiceValidatorConditions.verifyInvoiceTransactionListSize(invoiceDto)){
            reasons.append("List of transactions does not match the number of transactions in the invoice");
        }

        if (invoiceValidatorConditions.verifyInvoiceNetAmount(invoiceDto)){
            reasons.append("Net ammount does not match");
        }

        if (invoiceValidatorConditions.verifyInvoiceGstAmount(invoiceDto)){
            reasons.append("GST ammount does not match");
        }

        if (invoiceValidatorConditions.verifyTotalAmountInvoice(invoiceDto)){
            reasons.append("Invoice Gross ammount does not match");
        }

        if (reasons.isEmpty()){
            return Optional.empty();
        } else {
            return Optional.of(reasons.toString());
        }
    }
}