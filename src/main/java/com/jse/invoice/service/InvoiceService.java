package com.jse.invoice.service;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.entity.Invoice;
import com.jse.invoice.entity.Transaction;
import com.jse.invoice.repository.InvoiceRepository;
import com.jse.invoice.utils.InvoiceValidator;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceValidator invoiceValidator;

    public InvoiceService(InvoiceRepository invoiceRepository, InvoiceValidator invoiceValidator) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceValidator = invoiceValidator;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoice(long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public String getInvoiceStatus(long id) {
        Invoice invoice = getInvoice(id);

        if (invoice != null){
            return "Valid: " + invoice.isValid() + " - Reason: " + invoice.getReason();

        }

        return null;
    }

    @Transactional
    public Invoice saveInvoice(InvoiceDto invoiceDto) {

        String invalidReason = invoiceValidator.verifyInvoice(invoiceDto);
        
        // creating the invoice
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

        return invoiceRepository.save(invoice);
    }
}
