package com.jse.invoice.utils;

import org.springframework.stereotype.Component;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.entity.Invoice;
import com.jse.invoice.entity.Transaction;

@Component
public class InvoiceGenerator {

    private final InvoiceValidator invoiceValidator;

    public InvoiceGenerator(InvoiceValidator invoiceValidator) {
        this.invoiceValidator = invoiceValidator;
    }

    public Invoice generateInvoice(InvoiceDto invoiceDto){
        String invalidReason = invoiceValidator.verifyInvoice(invoiceDto).orElse(null);
        
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceDto.invoiceNumber());
        invoice.setGrossAmount(invoiceDto.gstAmount().add(invoiceDto.netAmount()));
        invoice.setNetAmount(invoiceDto.netAmount());
        invoice.setGstAmount(invoiceDto.gstAmount());
        invoice.setTotalNoTransactions(invoiceDto.totalNoTransactions());

        invoiceDto.transactionDtoList().forEach(
            transactionDto -> {
                Transaction transaction = new Transaction();
                transaction.setDateReceived(transactionDto.dateReceived());
                transaction.setTransactionDate(transactionDto.transactionDate());
                transaction.setBillingPeriodStart(transactionDto.billingPeriodStart());
                transaction.setBillingPeriodEnd(transactionDto.billingPeriodEnd());
                transaction.setNetTransactionAmount(transactionDto.netTransactionAmount());
                transaction.setGstAmount(transactionDto.gstAmount());
                transaction.setInvoice(invoice);

                invoice.getTransactions().add(transaction);
            }
        );

        if (invalidReason != null){
            invoice.setValid(false);
            invoice.setReason(invalidReason);
        } else {
            invoice.setValid(true);
        }

        return invoice;
        
    }

}
