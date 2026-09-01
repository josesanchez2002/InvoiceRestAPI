package com.jse.invoice.service;

import com.jse.invoice.dto.InvoiceDto;
import com.jse.invoice.entity.Invoice;
import com.jse.invoice.entity.Transaction;
import com.jse.invoice.repository.InvoiceRepository;
import com.jse.invoice.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final TransactionRepository transactionRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, TransactionRepository transactionRepository) {
        this.invoiceRepository = invoiceRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoice(long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public String getInvoiceStatus(long id) {
        Invoice invoice = getInvoice(id);

        if (invoice != null) {
            List<Transaction> transactionList = transactionRepository.findTransactionsByInvoiceId(invoice.getId());

            if (transactionList != null && !transactionList.isEmpty()) {
                if (transactionList.size() != invoice.getTotalNoTransactions()) {
                    return "Invalid - Number of transactions in the invoice does not match the total number of transactions recorded against this invoice";
                }

                BigDecimal totalNetTransactionAmt = transactionList.stream()
                        .map(Transaction::getNetTransactionAmount) // Extract BigDecimal
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalGSTTransactionAmt = transactionList.stream()
                        .map(Transaction::getGstAmount) // Extract BigDecimal
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (invoice.getNetAmount().compareTo(totalNetTransactionAmt) != 0) {
                    System.out.println(invoice.getNetAmount());

                    System.out.println(totalNetTransactionAmt);
                    return "Invalid - Net amount do not match";
                }

                if (invoice.getGstAmount().compareTo(totalGSTTransactionAmt) != 0) {
                    return "Invalid - GST amount do not match";
                }
            }

        }

        return "Valid";
    }

    @Transactional
    public Invoice saveInvoice(InvoiceDto invoiceDto) {
        // creating the invoice

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceDto.invoiceNumber());
        invoice.setGrossAmount(invoiceDto.gstAmount().add(invoiceDto.netAmount()));
        invoice.setNetAmount(invoiceDto.netAmount());
        invoice.setGstAmount(invoiceDto.gstAmount());
        invoice.setTotalNoTransactions(invoiceDto.totalNoTransactions());

        return invoiceRepository.save(invoice);
    }
}
